package com.boti.productmanagerapp.infrastructure.config;

import com.boti.productmanagerapp.adapters.out.batchprocess.FileStreamPortImpl;
import com.boti.productmanagerapp.adapters.out.repository.ProductRepositoryRepository;
import com.boti.productmanagerapp.application.core.usecases.InsertProductUsecase;
import com.boti.productmanagerapp.application.core.usecases.ProductIngestionUseCase;
import com.boti.productmanagerapp.application.core.usecases.ProductQueryUsecase;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import com.boti.productmanagerapp.application.ports.out.ReadProductFile;
import com.boti.productmanagerapp.infrastructure.LoggerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public LoggerPort loggerPort() {
        return new LoggerAdapter();
    }

    @Bean
    public InsertProductUsecase insertProductUsecase(ProductRepositoryPort productRepositoryPort) {
        return new InsertProductUsecase(productRepositoryPort);
    }

    @Bean
    public ProductIngestionUseCase ProductIngestionUseCase(ProductRepositoryRepository productRepository, LoggerPort loggerAdapter, FileStreamPortImpl fileStreamPort, ReadProductFile productFile) {
        return new ProductIngestionUseCase(productRepository, loggerAdapter, fileStreamPort, productFile);
    }

    @Bean
    public ProductQueryUsecase productQueryUsecase(LoggerPort loggerPort, ProductRepositoryPort productRepository) {
        return new ProductQueryUsecase(loggerPort, productRepository);
    }
}
