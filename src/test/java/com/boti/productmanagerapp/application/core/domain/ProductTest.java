package com.boti.productmanagerapp.application.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Should return the total amount of a product")
    void shouldCreateExceptionWithCorrectMessage() {
        Product product = new Product(1L,"RTIX", 25, "$0.67", "3XL", "Industrial Specialties", "LA");
        var value = product.getTotalValue();
        assertEquals(value, new BigDecimal("16.75"));
    }
}