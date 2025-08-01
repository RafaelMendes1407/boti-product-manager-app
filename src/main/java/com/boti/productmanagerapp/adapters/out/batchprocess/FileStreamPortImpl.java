package com.boti.productmanagerapp.adapters.out.batchprocess;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.FileStreamPort;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductResult;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class FileStreamPortImpl implements FileStreamPort {
    @Autowired
    private LoggerPort log;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Future<ProductResult>> startStream(List<File> files) {
        log.info(FileStreamPortImpl.class, "Starting file Streaming...");
        return files.parallelStream()
                .flatMap(file -> processFileStreaming(file).stream())
                .collect(Collectors.toList());
    }

    public void finishStreamFile() {
        ExecutorServiceSingleton.shutdown();
    }

    private List<Future<ProductResult>> processFileStreaming(File file) {
        ExecutorService executor = ExecutorServiceSingleton.getInstance();
        List<Future<ProductResult>> futures = new ArrayList<>();

        try (JsonParser parser = objectMapper.getFactory().createParser(file)) {

            while (!parser.isClosed() && parser.nextToken() != JsonToken.FIELD_NAME) {
                // searchs for "data" field
            }

            if (!"data".equals(parser.getCurrentName())) {
                throw new IOException("Field data not found: " + file.getName());
            }

            parser.nextToken();

            while (parser.nextToken() == JsonToken.START_OBJECT) {
                Product product = objectMapper.readValue(parser, Product.class);
                futures.add(executor.submit(() -> new ProductResultImpl(product, null)));
            }
            log.info(FileStreamPortImpl.class, String.format("File %s read finished", file.getName()));
        } catch (IOException e) {
            log.error(FileStreamPortImpl.class, String.format("Erro ao fazer streaming do arquivo %s: error: %s", file.getName(), e.getMessage()), e);
            futures.add(executor.submit(() -> new ProductResultImpl(null, e)));
        }
        return futures;
    }
}
