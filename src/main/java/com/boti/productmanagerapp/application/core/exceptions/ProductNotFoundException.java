package com.boti.productmanagerapp.application.core.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String name) {
        super(String.format("Product: %s not found", name));
    }
}
