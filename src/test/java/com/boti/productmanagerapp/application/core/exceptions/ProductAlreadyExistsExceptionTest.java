package com.boti.productmanagerapp.application.core.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Should create exception with correct message for product and ID")
    void shouldCreateExceptionWithCorrectMessage() {
        String productName = "RTIX";
        long productId = 123L;
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(productName);
        assertNotNull(exception, "Exception should not be null");
        String expectedMessage = String.format("Product: %s already exists", productName);
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match the expected format");
        assertTrue(exception instanceof RuntimeException, "Exception should be an instance of RuntimeException");
    }


}