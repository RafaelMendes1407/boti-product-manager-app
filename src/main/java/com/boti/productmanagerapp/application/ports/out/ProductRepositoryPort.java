package com.boti.productmanagerapp.application.ports.out;

import com.boti.productmanagerapp.application.core.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product save (Product product);
    Optional<Product> findByProductId(long id);
    Product findByProductName(String name);
    List<Product> findByRange(BigDecimal startRange, BigDecimal endRange);
    Long count();

}


