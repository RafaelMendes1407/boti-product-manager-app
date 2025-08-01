package com.boti.productmanagerapp.adapters.out.batchprocess;

import com.boti.productmanagerapp.application.core.exceptions.FileProductProcessorException;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.application.ports.in.ReadProductFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Component
public class ReadProductFileImpl implements ReadProductFile {

    @Autowired
    private LoggerPort log;

    @Override
    public List<File> getInputDataFiles(String directoryPath) {
        log.info(ReadProductFileImpl.class, String.format("Starting json file scan path: %s", directoryPath));
        File folder = new File(directoryPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null) {
            throw new FileProductProcessorException("Error while scanning for files: invalid files or directory.");
        }
        log.info(ReadProductFileImpl.class, String.format("File scan finished: %d file(s) found for insertion", files.length));
        return Arrays.asList(files);
    }

}