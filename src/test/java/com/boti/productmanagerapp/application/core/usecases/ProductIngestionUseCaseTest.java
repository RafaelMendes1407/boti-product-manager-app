package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.adapters.out.batchprocess.ProductResultImpl;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.ports.out.FileStreamPort;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import com.boti.productmanagerapp.application.ports.out.ProductResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ProductIngestionUseCaseTest {

    private ProductRepositoryPort repository;
    private LoggerPort loggerPort;
    private FileStreamPort fileStreamPort;
    private ProductIngestionUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepositoryPort.class);
        loggerPort = mock(LoggerPort.class);
        fileStreamPort = mock(FileStreamPort.class);

        useCase = new ProductIngestionUseCase(repository, loggerPort, fileStreamPort);
    }

    @Test
    void deveProcessarProdutoCorretamente() throws Exception {
        Product product = this.createProduct();
        ProductResult result = new ProductResultImpl(product, null);
        Future<ProductResult> future = CompletableFuture.completedFuture(result);

        when(fileStreamPort.startStream(Mockito.<List<File>>any())).thenReturn(List.of(future));
        when(repository.save(any(Product.class))).thenReturn(product);

        useCase.execute(List.of(new File("dummy.json")));

        verify(repository, times(1)).save(product);
        verify(loggerPort, times(1)).info(eq(ProductIngestionUseCase.class), contains("Starting"));
        verify(loggerPort, times(1)).info(eq(ProductIngestionUseCase.class), contains("finished"));
    }

    @Test
    void deveLogarQuandoProdutoJaExiste() throws Exception {
        Product product = this.createProduct();
        ProductResult result = new ProductResultImpl(product, null);
        Future<ProductResult> future = CompletableFuture.completedFuture(result);

        when(fileStreamPort.startStream(Mockito.<List<File>>any())).thenReturn(List.of(future));
        when(repository.save(any(Product.class))).thenThrow(new ProductAlreadyExistsException(product.getProduct(), product.getProductId()));

        useCase.execute(List.of(new File("dummy.json")));

        verify(repository, times(1)).save(product);
        verify(loggerPort).warn(eq(ProductIngestionUseCase.class), contains("already exists"));
    }

    @Test
    void deveLogarErroAoProcessarArquivo() throws Exception {
        Product product = this.createProduct();
        Exception processingException = new RuntimeException("Erro na leitura");

        ProductResult result = new ProductResultImpl(product, processingException);
        Future<ProductResult> future = CompletableFuture.completedFuture(result);

        when(fileStreamPort.startStream(Mockito.<List<File>>any())).thenReturn(List.of(future));

        useCase.execute(List.of(new File("arquivo_com_erro.json")));

        verify(loggerPort).warn(eq(ProductIngestionUseCase.class), contains("Erro na leitura"));
        verify(repository, never()).save(any());
    }

    @Test
    void deveLogarErroDeThreadAoExecutarFuturo() throws Exception {
        Future<ProductResult> future = mock(Future.class);
        when(future.get()).thenThrow(new InterruptedException("Thread interrompida"));
        when(fileStreamPort.startStream(Mockito.<List<File>>any())).thenReturn(List.of(future));

        useCase.execute(List.of(new File("qualquer.json")));

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