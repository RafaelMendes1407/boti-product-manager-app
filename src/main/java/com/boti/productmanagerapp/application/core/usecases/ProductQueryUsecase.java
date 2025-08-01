package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.adapters.in.web.response.ProductResponse;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import com.boti.productmanagerapp.utils.mappers.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public class ProductQueryUsecase {

    private final LoggerPort log;
    private final ProductRepositoryPort productRepositoryPort;

    public ProductQueryUsecase(LoggerPort log, ProductRepositoryPort productRepositoryPort) {
        this.log = log;
        this.productRepositoryPort = productRepositoryPort;
    }

    public Page<ProductResponse> execute(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable page) {
        log.info(ProductQueryUsecase.class, "Starting product query");
        return productRepositoryPort.queryByNameOrPriceRange(name, minPrice, maxPrice, page).map(
                ProductMapper.Instance::toProductResponse
        );
    }
}
