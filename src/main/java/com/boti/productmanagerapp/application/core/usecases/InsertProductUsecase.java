package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;

public class InsertProductUsecase {

    private final ProductRepositoryPort productRepositoryPort;

    public InsertProductUsecase(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public Product execute(Product product) {
        return productRepositoryPort.save(product);
    }

}
