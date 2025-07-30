package com.boti.productmanagerapp.application.core.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String product, long id) {
        super(String.format("Product: %s already exists with ID %d", product, id));
    }

}
