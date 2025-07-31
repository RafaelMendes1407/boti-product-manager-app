package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.ProductPort;

public class InsertProductUsecase {

    private final ProductPort productPort;

    public InsertProductUsecase(ProductPort productPort) {
        this.productPort = productPort;
    }

    public Product execute(Product product) {
        return productPort.save(product);
    }

}
