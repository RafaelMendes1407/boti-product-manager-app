package com.boti.productmanagerapp.application.ports.out;

import com.boti.productmanagerapp.application.core.domain.Product;

import java.util.List;

public interface ProductPersistencePort {


    void insertRecordsBatch(List<Product> products);
    String[] getFiles();
}
