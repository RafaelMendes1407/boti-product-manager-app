package com.boti.productmanagerapp.application.core.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with correct message for product and ID")
    void shouldCreateExceptionWithCorrectMessage() {
        String productName = "RTIX";
        ProductNotFoundException exception = new ProductNotFoundException(productName);
        assertNotNull(exception, "Exception should not be null");
        String expectedMessage = String.format("Product: %s not found", productName);
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match the expected format");
        assertTrue(exception instanceof RuntimeException, "Exception should be an instance of RuntimeException");
    }


}