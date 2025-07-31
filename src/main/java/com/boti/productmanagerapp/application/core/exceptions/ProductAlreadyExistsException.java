package com.boti.productmanagerapp.application.core.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String product) {
        super(String.format("Product: %s already exists", product));
    }

}
