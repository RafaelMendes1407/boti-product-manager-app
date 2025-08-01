package com.boti.productmanagerapp.adapters.in.controller;

import com.boti.productmanagerapp.adapters.out.entities.ProductEntity;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class FindProductByIdControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepositoryPort productRepository;

    @Test
    @DisplayName("Should return product by ID")
    void shouldReturnProductById() throws Exception {
        Product product = new Product();
        product.setProduct("TestProduct");
        product.setPrice("$10.00");
        product.setQuantity(10L);
        product.setType("UniqueType");
        product.setIndustry("Tech");
        product.setOrigin("SP");

        Product saved = productRepository.save(product);

        mockMvc.perform(get("/v1/products/" + saved.getProductId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("TestProduct"))
                .andExpect(jsonPath("$.price").value("10.00"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    @DisplayName("Should return 404 when product not found")
    void shouldReturn404IfNotFound() throws Exception {
        mockMvc.perform(get("/v1/products/999999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}