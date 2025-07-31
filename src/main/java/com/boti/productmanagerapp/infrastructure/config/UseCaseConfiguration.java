package com.boti.productmanagerapp.infrastructure.config;

import com.boti.productmanagerapp.application.core.usecases.InsertProductUsecase;
import com.boti.productmanagerapp.application.ports.out.ProductPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public InsertProductUsecase insertProductUsecase(ProductPort productPort) {
        return new InsertProductUsecase(productPort);
    }
}
