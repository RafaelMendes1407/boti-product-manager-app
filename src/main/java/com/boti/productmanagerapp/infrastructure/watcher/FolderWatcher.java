package com.boti.productmanagerapp.infrastructure.watcher;

import com.boti.productmanagerapp.application.core.usecases.ProductIngestionUseCase;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.*;

@Component
public class FolderWatcher {


    @Value("${app.ingestion.folder.location}")
    private String folderPath;

    private final ProductIngestionUseCase productIngestionUseCase;
    private final LoggerPort log;

    @Autowired
    public FolderWatcher(ProductIngestionUseCase productIngestionUseCase, LoggerPort log) {
        this.productIngestionUseCase = productIngestionUseCase;
        this.log = log;
    }

    @PostConstruct
    public void startWatching() {
        Thread thread = new Thread(this::watchDirectory);
        thread.setDaemon(true);
        thread.start();
    }

    private void watchDirectory() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(folderPath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            log.info(FolderWatcher.class, "Watching directory: " + folderPath);

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path fileName = (Path) event.context();

                        if (fileName.toString().endsWith(".tmp")) {
                            // Ignora arquivos temporários
                            continue;
                        }

                        Path fullPath = path.resolve(fileName);
                        log.info(FolderWatcher.class, "New file detected: " + fullPath);

                        try {
                            waitUntilFileIsReady(fullPath.toFile());
                            productIngestionUseCase.execute(fullPath.toFile());
                        } catch (Exception e) {
                            log.error(FolderWatcher.class, "Error processing file: " + fullPath + ", reason: " + e.getMessage());
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) break;
            }

        } catch (Exception e) {
            log.error(FolderWatcher.class, "Error watching directory: " + e.getMessage());
        }
    }

    private void waitUntilFileIsReady(File file) throws InterruptedException {
        int maxRetries = 10;
        long previousLength = -1;

        for (int i = 0; i < maxRetries; i++) {
            long length = file.length();

            if (length > 0 && length == previousLength) {
                return; // tamanho estabilizou
            }

            previousLength = length;
            Thread.sleep(300); // aguarda antes de tentar novamente
        }

        throw new RuntimeException("File is not stable for reading: " + file.getName());
    }
}