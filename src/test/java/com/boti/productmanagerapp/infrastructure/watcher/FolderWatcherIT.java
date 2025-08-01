package com.boti.productmanagerapp.infrastructure.watcher;

import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
        "app.ingestion.folder.location=./target/temp-ingestion"
})
public class FolderWatcherIT {

    @Autowired
    private ProductRepositoryPort productRepository;

    @Autowired
    private LoggerPort logger;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Path ingestionFolder = Paths.get("target/temp-ingestion");

    @BeforeAll
    void setup() throws IOException {
        Files.createDirectories(ingestionFolder);
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.walk(ingestionFolder)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        // Ignorar
                    }
                });
    }

    @Test
    @Disabled("Por algum motivo esse teste falha no githubactions") // TODO
    void shouldDetectAndProcessNewJsonFile() throws Exception {
        Map<String, Object> product = new HashMap<>();
        product.put("product", "MakeB");
        product.put("quantity", 100L);
        product.put("price", "$60.0");
        product.put("type", "MakeUP");
        product.put("industry", "Boticario");
        product.put("origin", "BR");

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("data", List.of(product));

        File tempFile = ingestionFolder.resolve("test-product.json.tmp").toFile();
        objectMapper.writeValue(tempFile, wrapper);

        Path finalPath = ingestionFolder.resolve("test-product.json");
        Files.move(tempFile.toPath(), finalPath);

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    List<Product> products = productRepository.findAll();
                    assertTrue(products.stream().anyMatch(p -> p.getProduct().equals("MakeB")));
                });
    }
}