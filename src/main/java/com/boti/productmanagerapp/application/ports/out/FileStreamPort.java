package com.boti.productmanagerapp.application.ports.out;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

public interface FileStreamPort {

    List<Future<ProductResult>> startStream(List<File> files);

}
