package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.exceptions.FileProductProcessorException;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.ports.in.ReadProductFile;
import com.boti.productmanagerapp.application.ports.out.*;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProductIngestionUseCase {

    private final ProductRepositoryPort repository;
    private final LoggerPort log;
    private final FileStreamPort fileStreamPort;
    private final ReadProductFile productFile;

    public ProductIngestionUseCase(ProductRepositoryPort repository, LoggerPort log, FileStreamPort fileStreamPort, ReadProductFile productFile) {
        this.repository = repository;
        this.log = log;
        this.fileStreamPort = fileStreamPort;
        this.productFile = productFile;
    }


    public void execute(String path) {
        try {
            List<File> files = productFile.getInputDataFiles(path);
            processFiles(files);
        } catch (FileProductProcessorException e) {
            log.error(ProductIngestionUseCase.class, String.format("File processor error: %s", e.getMessage()));
        }
    }

    public void execute(File file) {
        processFiles(Collections.singletonList(file));
    }

    private void processFiles(List<File> files) {
        log.info(ProductIngestionUseCase.class, "Starting product Ingestion data process");

        try {
            List<Future<ProductResult>> futures = fileStreamPort.startStream(files);

            for (Future<ProductResult> f : futures) {
                try {
                    ProductResult productResult = f.get();

                    if (!productResult.hasError()) {
                        Product product = productResult.getProduct();

                        try {
                            product = repository.save(product);
                        } catch (ProductAlreadyExistsException ex) {
                            log.warn(ProductIngestionUseCase.class, String.format("Product %s already exists", product.getProduct()));
                        }

                    } else {
                        Exception ex = productResult.getException();
                        log.warn(ProductIngestionUseCase.class, String.format("File processor error: %s", ex.getMessage()));
                    }

                } catch (ExecutionException | InterruptedException e) {
                    log.error(
                            ProductIngestionUseCase.class,
                            String.format("File processor error: %s", e.getMessage()),
                            new FileProductProcessorException(e.getMessage()));
                }
            }

        } finally {
            log.info(ProductIngestionUseCase.class, "Product Ingestion finished");
            fileStreamPort.finishStreamFile();
        }
    }
}