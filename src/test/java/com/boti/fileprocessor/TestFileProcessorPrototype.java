package com.boti.fileprocessor;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class TestFileProcessorPrototype {

    private static final String DIRECTORY_PATH = "src/test/resources"; // pasta com arquivos .json
    private static final int THREADS = Runtime.getRuntime().availableProcessors();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    void shouldCreateExceptionWithCorrectMessage() {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        try {
            List<File> jsonFiles = getJsonFiles(DIRECTORY_PATH);

            List<Future<Object>> futures = jsonFiles.stream()
                    .map(file -> executor.submit(() -> {
                        processJsonFile(file);
                        return null;
                    }))
                    .collect(Collectors.toList());

            // Aguarda todos terminarem
            for (Future<Object> f : futures) {
                try {
                    System.out.println(f.get());
                } catch (ExecutionException | InterruptedException e) {
                    System.err.println("Erro ao processar um arquivo: " + e.getMessage());
                }
            }

        } finally {
            executor.shutdown();
        }
    }

    private static List<File> getJsonFiles(String directoryPath) {
        File folder = new File(directoryPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null) {
            throw new RuntimeException("Diretório inválido ou erro ao listar arquivos.");
        }

        return Arrays.asList(files);
    }

    private static void processJsonFile(File file) {
        try {
            System.out.println("Processando: " + file.getName());
            Product product = objectMapper.readValue(file, Product.class);
            // Simule o processamento aqui
            System.out.println("Conteúdo: " + product);

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo " + file.getName() + ": " + e.getMessage());
        }
    }
}
