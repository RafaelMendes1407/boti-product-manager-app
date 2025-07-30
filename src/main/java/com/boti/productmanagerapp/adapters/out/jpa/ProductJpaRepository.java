package com.boti.productmanagerapp.adapters.out.jpa;

import com.boti.productmanagerapp.adapters.out.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByProduct(String name);
}
