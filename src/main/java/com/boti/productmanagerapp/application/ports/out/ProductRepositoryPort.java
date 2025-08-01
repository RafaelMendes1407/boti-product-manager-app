package com.boti.productmanagerapp.application.ports.out;

import com.boti.productmanagerapp.adapters.out.entities.ProductEntity;
import com.boti.productmanagerapp.application.core.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product save (Product product);
    Page<ProductEntity> queryByNameOrPriceRange(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable page);
    Optional<Product> findByProductId(long id);
    Product findByProductName(String name);
    List<Product> findByRange(BigDecimal startRange, BigDecimal endRange);
    Long count();

}


