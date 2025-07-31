package com.boti.productmanagerapp.application.core.usecases;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.ProductPersistencePort;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ProductIngestionUseCase {

    private ProductPersistencePort productPersistencePort;


    public void execute(File[] files, ExecutorService executor) {

        for (File file : files) {
            executor.submit(() -> {
//                try {
//                    // Use a sua lógica de leitura de JSON e inserção aqui
//                    // Exemplo simplificado:
//                    List<Product> recordsToInsert = new ArrayList<>();
//                    for (int i = 0; i < 1000; i++) { // Substitua pela sua lógica de leitura de streaming
//                        // Suponha que você leu um registro e o mapeou para YourRecordObject
//                        YourRecordObject record = new YourRecordObject(i, "Nome " + i);
//                        recordsToinsert.add(record);
//                    }
//                    new DatabaseInserter().insertRecordsBatch(recordsToInsert); // Chama o método de inserção em batch
//                    System.out.println("Processado arquivo: " + file.getName());
//                } catch (IOException | SQLException e) {
//                    System.err.println("Erro ao processar arquivo " + file.getName() + ": " + e.getMessage());
//                }
            });
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(60, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
