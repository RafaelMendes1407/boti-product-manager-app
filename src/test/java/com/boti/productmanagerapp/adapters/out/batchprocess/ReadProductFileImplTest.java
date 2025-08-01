package com.boti.productmanagerapp.adapters.out.batchprocess;

import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ReadProductFileImplTest {

    @Mock
    private LoggerPort log;

    @InjectMocks
    private ReadProductFileImpl readProductFile;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return files from resources directory")
    void shouldReturnJsonFilesFromResourceDirectory() throws IOException {
        String path = "src/test/resources";
        List<File> files = readProductFile.getInputDataFiles(path);

        assertNotNull(files);
        assertEquals(2, files.size());

        verify(log, times(1)).info(
                ReadProductFileImpl.class,
                String.format("Starting json file scan path: %s", path)
        );
        verify(log, times(1)).info(
                ReadProductFileImpl.class,
                "File scan finished: 2 file(s) found for insertion"
        );
    }

    @Test
    @DisplayName("Should return files from directory")
    void shouldReturnJsonFilesFromDirectory() throws IOException {
        Path jsonFile1 = tempDir.resolve("product1.json");
        Path jsonFile2 = tempDir.resolve("product2.json");
        Path otherFile = tempDir.resolve("document.txt");

        jsonFile1.toFile().createNewFile();
        jsonFile2.toFile().createNewFile();
        otherFile.toFile().createNewFile();

        List<File> files = readProductFile.getInputDataFiles(tempDir.toString());

        assertNotNull(files);
        assertEquals(2, files.size());
        assertTrue(files.stream().anyMatch(file -> file.getName().equals("product1.json")));
        assertTrue(files.stream().anyMatch(file -> file.getName().equals("product2.json")));

        verify(log, times(1)).info(
                ReadProductFileImpl.class,
                String.format("Starting json file scan path: %s", tempDir.toString())
        );
        verify(log, times(1)).info(
                ReadProductFileImpl.class,
                "File scan finished: 2 file(s) found for insertion"
        );
    }

    @Test
    @DisplayName("Should return an empty list when there aren't json files")
    void shouldReturnEmptyListWhenNoJsonFilesFound() throws IOException {
        Path otherFile1 = tempDir.resolve("document.txt");
        Path otherFile2 = tempDir.resolve("image.png");
        otherFile1.toFile().createNewFile();
        otherFile2.toFile().createNewFile();

        List<File> files = readProductFile.getInputDataFiles(tempDir.toString());

        assertNotNull(files);
        assertTrue(files.isEmpty());

        verify(log, times(1)).info(
                ReadProductFileImpl.class,
                String.format("Starting json file scan path: %s", tempDir.toString())
        );
        verify(log, times(1)).info(
                ReadProductFileImpl.class,
                "File scan finished: 0 file(s) found for insertion"
        );
    }

    @Test
    @DisplayName("Should Return an empty list when the directory is emty")
    void shouldReturnEmptyListWhenDirectoryIsEmpty() {
        List<File> files = readProductFile.getInputDataFiles(tempDir.toString());

        assertNotNull(files);
        assertTrue(files.isEmpty());

        verify(log, times(1)).info(
                ReadProductFileImpl.class,
                String.format("Starting json file scan path: %s", tempDir.toString())
        );
        verify(log, times(1)).info(
                ReadProductFileImpl.class,
                "File scan finished: 0 file(s) found for insertion"
        );
    }

    @Test
    @DisplayName("Should throw an exception when the path doesn't exists")
    void shouldThrowExceptionForInvalidDirectoryPath() {
        String invalidPath = "/caminho/que/nao/existe/123";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            readProductFile.getInputDataFiles(invalidPath);
        });

        assertEquals("Error processing product error: Error while scanning for files: invalid files or directory.", exception.getMessage());

        verify(log, times(1)).info(
                ReadProductFileImpl.class,
                String.format("Starting json file scan path: %s", invalidPath)
        );
        // O log de "finished" não deve ser chamado, pois a exceção é lançada antes.
        verify(log, times(0)).info(
                ReadProductFileImpl.class,
                "File scan finished: 0 file(s) found for insertion"
        );
    }

}