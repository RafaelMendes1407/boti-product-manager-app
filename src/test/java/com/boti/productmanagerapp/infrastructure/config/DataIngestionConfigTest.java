package com.boti.productmanagerapp.infrastructure.config;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.usecases.ProductIngestionUseCase;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "app.ingestion.folder.location=src/test/resources/data_test_1",
        "app.ingestion.pool-size=4"
}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("default")
@TestPropertySource(locations = "classpath:application-test.properties")
class DataIngestionConfigTest {

    @Autowired
    private ProductRepositoryPort productRepository;

    @Autowired
    private ProductIngestionUseCase ingestionUseCase;

    @Test
    @DisplayName("Should load and persist all files in folder during application initialization")
    void shouldIngestDataOnContextStartup() {
        // Produtos ja devem ser inseridos durante a inicialização do contexto do spring
        // por isso é verificado o repositório diretamente
        List<Product> products = productRepository.findAll();
        int dataJsonItensCount = 31;
        assertFalse(products.isEmpty(), "Produtos deveriam ter sido inseridos");
        assertTrue(products.size() == dataJsonItensCount, "Deveria conter todos os produtos listados no json");
    }
}