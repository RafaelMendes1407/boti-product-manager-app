package com.boti.productmanagerapp.adapters.in.controller;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ProductQueryControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepositoryPort productRepository;

    @BeforeAll
    void setupDatabase() {
        productRepository.deleteAll();

        productRepository.saveAll(List.of(
                new Product("Alpha", 10, "$2.00", "TypeA", "IndustryA", "BR"),
                new Product("Beta", 15, "$3.50", "TypeB", "IndustryB", "US"),
                new Product("Gamma", 5, "$5.00", "TypeC", "IndustryC", "CA")
        ));
    }

    @Test
    @DisplayName("Should return products by name")
    void shouldReturnProductsFilteredByName() {
        String url = "/v1/products?name=alp";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Alpha"));
        assertFalse(response.getBody().contains("Beta"));
    }

    @Test
    @DisplayName("Should return products in price range")
    void shouldReturnProductsInPriceRange() {
        String url = "/v1/products?minPrice=2.00&maxPrice=4.00";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Alpha"));
        assertTrue(response.getBody().contains("Beta"));
        assertFalse(response.getBody().contains("Gamma"));
    }

    @Test
    @DisplayName("Should return Bad Request if no filter is provided")
    void shouldReturnBadRequestIfNoFiltersProvided() {
        String url = "/v1/products";

        ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return paginated Response")
    void shouldReturnPaginatedResponse() {
        String url = "/v1/products?minPrice=0.00&page=0&size=2";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Alpha") || response.getBody().contains("Beta"));
    }
}