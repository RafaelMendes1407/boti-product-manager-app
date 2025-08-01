package com.boti.productmanagerapp.application.ports.out;

import com.boti.productmanagerapp.application.core.domain.PageResult;
import com.boti.productmanagerapp.application.core.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepositoryPort {

    Product save (Product product);
    PageResult<Product> queryByNameOrPriceRange(String name, BigDecimal minPrice, BigDecimal maxPrice, int pageNumber, int pageSize);
    Product findByProductId(long id);
    Long count();
    void deleteAll();
    List<Product> findAll();
    void saveAll(List<Product> products);

}


