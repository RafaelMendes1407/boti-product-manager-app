package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.adapters.out.batchprocess.ProductResultImpl;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.exceptions.FileProductProcessorException;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.ports.in.ReadProductFile;
import com.boti.productmanagerapp.application.ports.out.FileStreamPort;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import com.boti.productmanagerapp.application.ports.out.ProductResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
    @Mock
    private ExecutorService executor;

    @InjectMocks
    private ProductIngestionUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

    @Test
    @DisplayName("Should process product correctly in async insert")
    void shouldProcessProduct() throws Exception {
        Product product = createProduct();
        ProductResult result = new ProductResultImpl(product, null);
        Future<ProductResult> futureInput = CompletableFuture.completedFuture(result);

        List<File> dummyFiles = List.of(new File("dummy.json"));
        when(productFile.getInputDataFiles(eq("path"))).thenReturn(dummyFiles);
        when(fileStreamPort.startStream(eq(dummyFiles))).thenReturn(List.of(futureInput));

        when(executor.submit(any(Callable.class))).thenAnswer((Answer<Future<Void>>) invocation -> {
            Callable<Void> task = invocation.getArgument(0);
            task.call();
            return CompletableFuture.completedFuture(null);
        });
        when(repository.save(any(Product.class))).thenReturn(product);

        useCase.execute("path");

        verify(repository, times(1)).save(product);
        verify(loggerPort).info(eq(ProductIngestionUseCase.class), contains("Starting"));
        verify(loggerPort).info(eq(ProductIngestionUseCase.class), contains("finished"));
    }

    @Test
    @DisplayName("Should log when product already exists")
    void shouldLogWhenProductAlreadyExists() throws Exception {
        Product product = createProduct();
        ProductResult result = new ProductResultImpl(product, null);
        Future<ProductResult> futureInput = CompletableFuture.completedFuture(result);

        List<File> dummyFiles = List.of(new File("dummy.json"));
        when(productFile.getInputDataFiles(eq("path"))).thenReturn(dummyFiles);
        when(fileStreamPort.startStream(eq(dummyFiles))).thenReturn(List.of(futureInput));

        when(executor.submit(any(Callable.class))).thenAnswer((Answer<Future<Void>>) invocation -> {
            Callable<Void> task = invocation.getArgument(0);
            task.call();
            return CompletableFuture.completedFuture(null);
        });
        when(repository.save(any(Product.class))).thenThrow(new ProductAlreadyExistsException(product.getProduct()));

        useCase.execute("path");

        verify(repository, times(1)).save(any(Product.class));
        verify(loggerPort).warn(eq(ProductIngestionUseCase.class), contains("already exists"));
    }

    @Test
    @DisplayName("Should log error when product has processing error")
    void shouldLogErrorWhenProcessingProduct() throws Exception {
        Product product = createProduct();
        Exception ex = new RuntimeException("Erro na leitura");
        ProductResult result = new ProductResultImpl(product, ex);
        Future<ProductResult> futureInput = CompletableFuture.completedFuture(result);

        List<File> dummyFiles = List.of(new File("arquivo_com_erro.json"));
        when(productFile.getInputDataFiles(eq("path"))).thenReturn(dummyFiles);
        when(fileStreamPort.startStream(eq(dummyFiles))).thenReturn(List.of(futureInput));

        when(executor.submit(any(Callable.class))).thenAnswer((Answer<Future<Void>>) invocation -> {
            Callable<Void> task = invocation.getArgument(0);
            task.call();
            return CompletableFuture.completedFuture(null);
        });

        useCase.execute("path");

        verify(loggerPort).warn(eq(ProductIngestionUseCase.class), contains("Erro na leitura"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should log error when future.get fails")
    void shouldLogErrorWhenFutureFails() throws Exception {
        Future<ProductResult> futureInput = mock(Future.class);
        when(futureInput.get()).thenThrow(new InterruptedException("Thread interrompida"));

        List<File> dummyFiles = List.of(new File("qualquer.json"));
        when(productFile.getInputDataFiles(eq("path"))).thenReturn(dummyFiles);
        when(fileStreamPort.startStream(eq(dummyFiles))).thenReturn(List.of(futureInput));

        when(executor.submit(any(Callable.class))).thenAnswer((Answer<Future<Void>>) invocation -> {
            Callable<Void> task = invocation.getArgument(0);
            task.call();
            return CompletableFuture.completedFuture(null);
        });

        useCase.execute("path");

        verify(loggerPort).error(eq(ProductIngestionUseCase.class), contains("Thread interrompida"), any());
    }

    @Test
    @DisplayName("Should log error on FileProductProcessorException")
    void shouldLogFileProductProcessorException() throws Exception {
        when(productFile.getInputDataFiles(anyString())).thenThrow(new FileProductProcessorException("Erro na leitura do arquivo"));

        useCase.execute("path");

        verify(loggerPort).error(eq(ProductIngestionUseCase.class), contains("Erro na leitura do arquivo"));
    }
}