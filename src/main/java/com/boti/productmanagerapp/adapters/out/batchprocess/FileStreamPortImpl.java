package com.boti.productmanagerapp.adapters.out.batchprocess;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.FileStreamPort;
import com.boti.productmanagerapp.application.ports.out.ProductResult;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class FileStreamPortImpl implements FileStreamPort {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    ExecutorService executor;

//    @Override
//    public List<Future<ProductResult>> startStream(List<File> files) {
//
//        return files.stream()
//                .map(file -> executor.submit(() -> {
//                    return processFile(file);
//                }))
//                .collect(Collectors.toList());
//    }

    @Override
    public List<Future<ProductResult>> startStream(List<File> files) {
        return files.stream()
                .flatMap(file -> processFileStreaming(file).stream())
                .collect(Collectors.toList());
    }

    public void finishStreamFile() {
        if (Objects.isNull(executor)) return;
        executor.shutdown();
    }

//    private ProductResult processFile(File file) {
//        try {
//            System.out.println("Processando: " + file.getName());
//            ProductWrapper product =  objectMapper.readValue(file, ProductWrapper.class);
//            return new ProductResultImpl(product.getData(), null);
//        } catch (IOException e) {
//            System.err.println("Erro ao ler o arquivo " + file.getName() + ": " + e.getMessage());
//            return new ProductResultImpl(null, e);
//        }
//    }

    private List<Future<ProductResult>> processFileStreaming(File file) {
        this.executor = Executors.newFixedThreadPool(4);
        List<Future<ProductResult>> futures = new ArrayList<>();

        try (JsonParser parser = objectMapper.getFactory().createParser(file)) {

            while (!parser.isClosed() && parser.nextToken() != JsonToken.FIELD_NAME) {
                // busca o campo "data"
            }

            if (!"data".equals(parser.getCurrentName())) {
                throw new IOException("Campo 'data' não encontrado no arquivo " + file.getName());
            }

            parser.nextToken();

            while (parser.nextToken() == JsonToken.START_OBJECT) {
                Product product = objectMapper.readValue(parser, Product.class);
                futures.add(executor.submit(() -> new ProductResultImpl(product, null)));
            }

        } catch (IOException e) {
            System.err.println("Erro ao fazer streaming do arquivo " + file.getName() + ": " + e.getMessage());
            futures.add(executor.submit(() -> new ProductResultImpl(null, e)));
        }

        return futures;
    }
}
