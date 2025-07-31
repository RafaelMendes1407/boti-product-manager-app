package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.FileStreamPort;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    @DisplayName("Should read and process a json file")
    void shouldReadAndSaveJsonDataInDatabase() {
        long jsonTotalProductsFileData1 = 31;
        ProductIngestionUseCase productIngestionUseCase = new ProductIngestionUseCase(productRepository, log, fileStreamPort);

        File file = new File("src/test/resources/data_1.json");
        List<File> fileList = new ArrayList<>();
        fileList.add(file);
        productIngestionUseCase.execute(fileList);

        assertEquals(jsonTotalProductsFileData1, productRepository.count());
    }

    @Test
    @DisplayName("Should read and process 2 json files and save only products that doesn't exists")
    void shouldReadAndSaveOnlyNewProducts() {
        long jsonTotalProductsFileData1 = 31;
        ProductIngestionUseCase productIngestionUseCase = new ProductIngestionUseCase(productRepository, log, fileStreamPort);

        File file = new File("src/test/resources/data_1.json");
        File file2 = new File("src/test/resources/data_1.json");
        List<File> fileList = new ArrayList<>();
        fileList.add(file);
        fileList.add(file2);
        productIngestionUseCase.execute(fileList);

        assertEquals(jsonTotalProductsFileData1, productRepository.count());
        verify(productRepository, times(62)).save(any(Product.class));
    }
}