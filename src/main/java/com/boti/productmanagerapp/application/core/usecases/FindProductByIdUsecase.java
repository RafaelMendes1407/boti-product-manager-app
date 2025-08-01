package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;

public class FindProductByIdUsecase {

    private final LoggerPort log;
    private final ProductRepositoryPort productRepository;

    public FindProductByIdUsecase(LoggerPort log, ProductRepositoryPort productRepository) {
        this.log = log;
        this.productRepository = productRepository;
    }

    public Product execute(long id) {
        log.info(FindProductByIdUsecase.class, String.format("Starting query for product id: %s", id));

        return this.productRepository.findByProductId(id);
    }

}
