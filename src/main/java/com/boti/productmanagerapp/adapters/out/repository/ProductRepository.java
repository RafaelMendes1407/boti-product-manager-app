package com.boti.productmanagerapp.adapters.out.repository;

import com.boti.productmanagerapp.adapters.out.entities.ProductEntity;
import com.boti.productmanagerapp.adapters.out.exceptions.DatabasePersistenceException;
import com.boti.productmanagerapp.adapters.out.jpa.ProductJpaRepository;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.core.exceptions.ProductNotFoundException;
import com.boti.productmanagerapp.application.ports.out.ProductPort;
import com.boti.productmanagerapp.utils.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ProductRepository implements ProductPort {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        try {
            ProductEntity productEntity = ProductMapper.Instance.toProductEntity(product);
            Product prod =  ProductMapper.Instance.toProduct(this.productJpaRepository.save(productEntity));
            return prod;
        } catch (DataIntegrityViolationException e) {
            Product existentProduct = this.findByProductName(product.getProduct());
            throw new ProductAlreadyExistsException(product.getProduct(), existentProduct.getProductId());
        } catch (Exception e) {
            throw new DatabasePersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<Product> findByProductId(long id) {

        return Optional.empty();
    }

    @Override
    public Product findByProductName(String name) {
        Optional<ProductEntity> productEntityOptional = this.productJpaRepository.findByProduct(name);
        ProductEntity foundEntity = productEntityOptional.orElseThrow(
                () -> new ProductNotFoundException(name)
        );

        return ProductMapper.Instance.toProduct(foundEntity);
    }

    @Override
    public List<Product> findByRange(BigDecimal startRange, BigDecimal endRange) {
        return List.of();
    }
}
