package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.application.core.domain.PageResult;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;

import java.math.BigDecimal;

public class ProductQueryUsecase {

    private final LoggerPort log;
    private final ProductRepositoryPort productRepositoryPort;

    public ProductQueryUsecase(LoggerPort log, ProductRepositoryPort productRepositoryPort) {
        this.log = log;
        this.productRepositoryPort = productRepositoryPort;
    }

    public PageResult<Product> execute(String name, BigDecimal minPrice, BigDecimal maxPrice, int pageNumber, int pageSize) {
        log.info(ProductQueryUsecase.class, "Starting product query");
        return productRepositoryPort.queryByNameOrPriceRange(name, minPrice, maxPrice, pageNumber, pageSize);
    }
}
