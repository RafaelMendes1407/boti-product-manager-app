package com.boti.productmanagerapp.adapters.out.batchprocess;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.ProductResult;

public class ProductResultImpl implements ProductResult {

    private final Product product;
    private final Exception exception;

    public ProductResultImpl(Product product, Exception exception) {
        this.product = product;
        this.exception = exception;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public boolean hasError() {
        return exception != null;
    }
}
