package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.FileStreamPort;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import com.boti.productmanagerapp.application.ports.in.ReadProductFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class ProductIngestionUseCaseTestIT {

    @Autowired
    private ProductIngestionUseCase ingestionUseCase;

    @Autowired
    private LoggerPort log;

    @SpyBean
    @Autowired
    private ProductRepositoryPort productRepository;

    @Autowired
    private FileStreamPort fileStreamPort;

    @Autowired
    private ReadProductFile readProductFile;

    @BeforeEach
    void setUp() {
        this.productRepository.deleteAll();
    }

    @Test
    @DisplayName("Should read and process a json file")
    void shouldReadAndSaveJsonDataInDatabase() {
        long jsonTotalProductsFileData1 = 31;
        ProductIngestionUseCase productIngestionUseCase = new ProductIngestionUseCase(productRepository, log, fileStreamPort, readProductFile);
        productIngestionUseCase.execute("src/test/resources/data_test_1");
        assertEquals(jsonTotalProductsFileData1, productRepository.count());
    }

    @Test
    @DisplayName("Should read and process 2 json files and save only products that doesn't exists")
    void shouldReadAndSaveOnlyNewProducts() {
        long jsonTotalProductsFileData1 = 31;
        ProductIngestionUseCase productIngestionUseCase = new ProductIngestionUseCase(productRepository, log, fileStreamPort, readProductFile);
        productIngestionUseCase.execute("src/test/resources/data_test_2");

        assertEquals(jsonTotalProductsFileData1, productRepository.count());
        verify(productRepository, times(62)).save(any(Product.class));
    }
}