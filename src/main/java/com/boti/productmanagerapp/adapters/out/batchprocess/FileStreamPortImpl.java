package com.boti.productmanagerapp.adapters.out.batchprocess;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.FileStreamPort;
import com.boti.productmanagerapp.application.ports.out.ProductResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class FileStreamPortImpl implements FileStreamPort {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    ExecutorService executor = Executors.newFixedThreadPool(4);

    @Override
    public List<Future<ProductResult>> startStream(List<File> files) {

        return files.stream()
                .map(file -> executor.submit(() -> {
                    return processFile(file);
                }))
                .collect(Collectors.toList());
    }

    public void finishStreamFile() {
        executor.shutdown();
    }

    private ProductResult processFile(File file) {
        try {
            System.out.println("Processando: " + file.getName());
            Product product =  objectMapper.readValue(file, Product.class);
            return new ProductResultImpl(product, null);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo " + file.getName() + ": " + e.getMessage());
            return new ProductResultImpl(null, e);
        }
    }
}
