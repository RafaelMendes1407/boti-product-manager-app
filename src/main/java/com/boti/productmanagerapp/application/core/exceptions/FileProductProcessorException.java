package com.boti.productmanagerapp.application.core.exceptions;

public class FileProductProcessorException extends RuntimeException {
    public FileProductProcessorException(String error) {
        super(String.format("Error processing product error: %s", error));
    }
}
