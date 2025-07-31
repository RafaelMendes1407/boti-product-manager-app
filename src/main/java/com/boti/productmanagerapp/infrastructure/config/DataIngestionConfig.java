package com.boti.productmanagerapp.infrastructure.config;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.usecases.ProductIngestionUseCase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Component
public class DataIngestionConfig implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(DataIngestionConfig.class);
    private static final int NUMBER_OF_DATA_FILES = 4;

    // private final ProductIngestionUseCase productIngestionUseCase;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final PlatformTransactionManager transactionManager;

    @Value("${app.ingestion.pool-size}")
    private int ingestionPoolSize;

    public DataIngestionConfig(// ProductIngestionUseCase productIngestionUseCase,
                               ResourceLoader resourceLoader,
                               ObjectMapper objectMapper,
                               PlatformTransactionManager transactionManager) {
        // this.productIngestionUseCase = productIngestionUseCase;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        this.transactionManager = transactionManager;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Starting product data ingestion process...");
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(ingestionPoolSize);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        IntStream.rangeClosed(1, NUMBER_OF_DATA_FILES).forEach(i -> {
            String fileName = String.format("classpath:data_%d.json", i);
            Resource resource = resourceLoader.getResource(fileName);

            if (resource.exists()) {
                futures.add(CompletableFuture.runAsync(() -> {
                    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
                    try {
                        log.info("Reading file: {}", fileName);
                        List<Product> products = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Product>>() {});

                        // productIngestionUseCase.execute(products);

                        log.info("Finished processing file: {}", fileName);
                        transactionManager.commit(status); // Commit da transação para esta thread
                    } catch (IOException e) {
                        log.error("Error reading or parsing JSON file {}: {}", fileName, e.getMessage(), e);
                        transactionManager.rollback(status);
                    } catch (Exception e) {
                        log.error("Unexpected error during file processing {}: {}", fileName, e.getMessage(), e);
                        transactionManager.rollback(status); // Rollback para outros erros
                    }
                }, executorService));
            } else {
                log.warn("Data file not found: {}", fileName);
            }
        });

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        executorService.shutdown();
        long endTime = System.currentTimeMillis();
        log.info("Product data ingestion process completed in {} ms.", (endTime - startTime));
    }
}