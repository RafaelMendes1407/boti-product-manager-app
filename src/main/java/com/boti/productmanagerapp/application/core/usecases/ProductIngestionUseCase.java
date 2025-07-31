package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.exceptions.FileProductProcessorException;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.ports.out.*;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProductIngestionUseCase {

    private final ProductRepositoryPort repository;
    private final LoggerPort loggerPort;
    private final FileStreamPort fileStreamPort;

    public ProductIngestionUseCase(ProductRepositoryPort repository, LoggerPort loggerPort, FileStreamPort fileStreamPort) {
        this.repository = repository;
        this.loggerPort = loggerPort;
        this.fileStreamPort = fileStreamPort;
    }

    public void execute(List<File> files) {
        loggerPort.info(ProductIngestionUseCase.class, "Starting product Ingestion");
        try {
            List<Future<ProductResult>> futures = fileStreamPort.startStream(files);

            for (Future<ProductResult> f : futures) {
                try {
                    ProductResult productResult = f.get();

                    if (!productResult.hasError()) {
                        Product product = productResult.getProduct();
                        try {
                            product = repository.save(productResult.getProduct());
                        } catch (ProductAlreadyExistsException ex) {
                            loggerPort.warn(ProductIngestionUseCase.class, String.format("Product %s already exists with id: %d", product.getProduct(), product.getProductId()));
                        }
                        continue;
                    }

                    Exception ex = productResult.getException();

                    loggerPort.warn(
                            ProductIngestionUseCase.class,
                            String.format("File processor error: %s", ex.getMessage()));
                } catch (ExecutionException | InterruptedException e) {
                    loggerPort.error(
                            ProductIngestionUseCase.class,
                            String.format("File processor error: %s", e.getMessage()), new FileProductProcessorException(e.getMessage()));
                }
            }
        } finally {
            loggerPort.info(ProductIngestionUseCase.class, "Product Ingestion finished");
            fileStreamPort.finishStreamFile();
        }
    }
}
