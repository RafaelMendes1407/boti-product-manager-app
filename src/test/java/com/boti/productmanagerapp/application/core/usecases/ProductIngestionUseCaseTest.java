package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.adapters.out.batchprocess.ProductResultImpl;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.ports.in.ReadProductFile;
import com.boti.productmanagerapp.application.ports.out.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductIngestionUseCaseTest {

    @Mock
    private ProductRepositoryPort repository;

    @Mock
    private LoggerPort loggerPort;

    @Mock
    private FileStreamPort fileStreamPort;

    @Mock
    private ReadProductFile productFile;

    @InjectMocks
    private ProductIngestionUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should process product correctly")
    void shouldProcessProduct() throws Exception {
        Product product = this.createProduct();
        ProductResult result = new ProductResultImpl(product, null);
        Future<ProductResult> future = CompletableFuture.completedFuture(result);

        when(fileStreamPort.startStream(Mockito.<List<File>>any())).thenReturn(List.of(future));
        when(repository.save(any(Product.class))).thenReturn(product);
        when(productFile.getInputDataFiles(anyString())).thenReturn(List.of(new File("dummy.json")));

        useCase.execute("path");

        verify(repository, times(1)).save(product);
        verify(loggerPort, times(1)).info(eq(ProductIngestionUseCase.class), contains("Starting"));
        verify(loggerPort, times(1)).info(eq(ProductIngestionUseCase.class), contains("finished"));
    }

    @Test
    @DisplayName("Should logs when the product already exists")
    void shouldLogsWhenProductAlreadyExists() throws Exception {
        Product product = this.createProduct();
        ProductResult result = new ProductResultImpl(product, null);
        Future<ProductResult> future = CompletableFuture.completedFuture(result);

        when(fileStreamPort.startStream(Mockito.<List<File>>any())).thenReturn(List.of(future));
        when(repository.save(any(Product.class))).thenThrow(new ProductAlreadyExistsException(product.getProduct()));
        when(productFile.getInputDataFiles(anyString())).thenReturn(List.of(new File("dummy.json")));
        useCase.execute("path");

        verify(repository, times(1)).save(product);
        verify(loggerPort).warn(eq(ProductIngestionUseCase.class), contains("already exists"));
    }

    @Test
    @DisplayName("Should log error when processing a file with error")
    void shouldLogErrorWHenProcessFile() throws Exception {
        Product product = this.createProduct();
        Exception processingException = new RuntimeException("Erro na leitura");

        ProductResult result = new ProductResultImpl(product, processingException);
        Future<ProductResult> future = CompletableFuture.completedFuture(result);

        when(fileStreamPort.startStream(Mockito.<List<File>>any())).thenReturn(List.of(future));
        when(productFile.getInputDataFiles(anyString())).thenReturn(List.of(new File("arquivo_com_erro.json")));

        useCase.execute("path");

        verify(loggerPort).warn(eq(ProductIngestionUseCase.class), contains("Erro na leitura"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should Log error when processing future")
    void shouldLogErrorWhenProcessingFuture() throws Exception {
        Future<ProductResult> future = mock(Future.class);
        when(future.get()).thenThrow(new InterruptedException("Thread interrompida"));
        when(fileStreamPort.startStream(Mockito.<List<File>>any())).thenReturn(List.of(future));
        when(productFile.getInputDataFiles(anyString())).thenReturn(List.of(new File("qualquer.json")));

        useCase.execute("path");

        verify(loggerPort).error(eq(ProductIngestionUseCase.class), contains("Thread interrompida"), any());
    }

    private Product createProduct() {
        return new Product(
                1L,
                "RXI",
                50L,
                "$60.00",
                "TX1",
                "CORP",
                "BR"
        );
    }

}