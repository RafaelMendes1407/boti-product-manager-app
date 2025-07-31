package com.boti.productmanagerapp.application.ports.out;

import java.io.File;
import java.util.List;

public interface ReadProductFile {
    List<File> getInputDataFiles(String directoryPath);
}
