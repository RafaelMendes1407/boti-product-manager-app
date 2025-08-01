package com.boti.productmanagerapp.adapters.in.controller;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class CreateProductControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepositoryPort productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a new product and return 201")
    void shouldCreateProduct() throws Exception {
        String payload = "{"
                + "\"product\": \"TEST-PRODUCT\","
                + "\"quantity\": 20,"
                + "\"price\": \"$9.99\","
                + "\"type\": \"XL\","
                + "\"industry\": \"Tech\","
                + "\"origin\": \"BR\""
                + "}";

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.product").value("TEST-PRODUCT"))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.price").value(9.99))
                .andExpect(jsonPath("$.type").value("XL"));

        assertEquals(1, productRepository.count());

        var product = productRepository.findAll().get(0);
        assertEquals("TEST-PRODUCT", product.getProduct());
        assertEquals("XL", product.getType());
        assertEquals("9.99", product.getPrice());
    }

    @Test
    @DisplayName("Should return 400 if payload is invalid")
    void shouldReturn400WhenPayloadIsInvalid() throws Exception {
        String invalidPayload = "{"
                + "\"quantity\": 10,"
                + "\"price\": \"$9.99\","
                + "\"type\": \"XL\""
                + "}";

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return conflict if the product and the type have already been inserted")
    void shouldReturnConflict() throws Exception {
        Product previoslyProduct = new Product("TEST-PRODUCT", 20, "$9.99", "XL", "Tech", "BR");
        this.productRepository.save(previoslyProduct);

        String payload = "{"
                + "\"product\": \"TEST-PRODUCT\","
                + "\"quantity\": 20,"
                + "\"price\": \"$9.99\","
                + "\"type\": \"XL\","
                + "\"industry\": \"Tech\","
                + "\"origin\": \"BR\""
                + "}";

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Product: TEST-PRODUCT already exists"));

        assertEquals(1, productRepository.count());

        var product = productRepository.findAll().get(0);
        assertEquals("TEST-PRODUCT", product.getProduct());
        assertEquals("XL", product.getType());
        assertEquals("9.99", product.getPrice());
    }
}