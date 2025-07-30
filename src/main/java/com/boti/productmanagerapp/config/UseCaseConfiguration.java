package com.boti.productmanagerapp.config;

import com.boti.productmanagerapp.application.core.usecases.InsertProduct;
import com.boti.productmanagerapp.application.ports.out.ProductPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public InsertProduct insertProductUsecase(ProductPort productPort) {
        return new InsertProduct(productPort);
    }
}
