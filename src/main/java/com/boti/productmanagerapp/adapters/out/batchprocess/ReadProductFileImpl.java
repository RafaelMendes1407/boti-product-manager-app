package com.boti.productmanagerapp.adapters.out.batchprocess;

import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.out.ReadProductFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Component
public class ReadProductFileImpl implements ReadProductFile {

    private LoggerPort loggerPort;

    @Override
    public List<File> readFile(String directoryPath) {
        File folder = new File(directoryPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null) {
            throw new RuntimeException("Diretório inválido ou erro ao listar arquivos.");
        }

        return Arrays.asList(files);
    }

}