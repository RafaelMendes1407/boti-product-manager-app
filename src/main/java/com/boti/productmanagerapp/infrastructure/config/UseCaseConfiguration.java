package com.boti.productmanagerapp.infrastructure.config;

import com.boti.productmanagerapp.adapters.out.batchprocess.FileStreamPortImpl;
import com.boti.productmanagerapp.adapters.out.repository.ProductRepository;
import com.boti.productmanagerapp.application.core.usecases.InsertProductUsecase;
import com.boti.productmanagerapp.application.core.usecases.ProductIngestionUseCase;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductPort;
import com.boti.productmanagerapp.infrastructure.LoggerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public LoggerPort loggerPort() {
        return  new LoggerAdapter();
    }

    @Bean
    public InsertProductUsecase insertProductUsecase(ProductPort productPort) {
        return new InsertProductUsecase(productPort);
    }

    @Bean
    public ProductIngestionUseCase ProductIngestionUseCase(ProductRepository productRepository, LoggerAdapter loggerAdapter, FileStreamPortImpl fileStreamPort) {
        return new ProductIngestionUseCase(productRepository, loggerAdapter, fileStreamPort);
    }
}
