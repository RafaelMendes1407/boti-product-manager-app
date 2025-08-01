package com.boti.productmanagerapp.adapters.out.repository;

import com.boti.productmanagerapp.adapters.out.entities.ProductEntity;
import com.boti.productmanagerapp.adapters.out.exceptions.DatabasePersistenceException;
import com.boti.productmanagerapp.adapters.out.jpa.ProductJpaRepository;
import com.boti.productmanagerapp.application.core.domain.PageResult;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.core.exceptions.ProductNotFoundException;
import com.boti.productmanagerapp.application.ports.out.ProductRepositoryPort;
import com.boti.productmanagerapp.utils.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryPortImpl implements ProductRepositoryPort {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public Product save(Product product) {
        try {
            ProductEntity productEntity = ProductMapper.Instance.toProductEntity(product);
            Product prod =  ProductMapper.Instance.toProduct(this.productJpaRepository.save(productEntity));
            return prod;
        } catch (DataIntegrityViolationException e) {
            throw new ProductAlreadyExistsException(product.getProduct());
        } catch (Exception e) {
            throw new DatabasePersistenceException(e.getMessage());
        }
    }

    @Override
    public PageResult<Product> queryByNameOrPriceRange(String name, BigDecimal minPrice, BigDecimal maxPrice, int pageNumber, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> cq = cb.createQuery(ProductEntity.class);
        Root<ProductEntity> root = cq.from(ProductEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(cb.like(cb.lower(root.get("product")), "%" + name.toLowerCase() + "%"));
        }

        if (minPrice != null) {
            predicates.add(cb.ge(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(cb.le(root.get("price"), maxPrice));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<ProductEntity> query = entityManager.createQuery(cq);
        query.setFirstResult(pageNumber);
        query.setMaxResults(pageSize);

        List<ProductEntity> resultList = query.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ProductEntity> countRoot = countQuery.from(ProductEntity.class);
        countQuery.select(cb.count(countRoot)).where(cb.and(predicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageResult<>(resultList.stream().map(ProductMapper.Instance::toProduct).collect(Collectors.toList()), pageNumber, pageSize, total);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Product findByProductId(long id) {
        return this.productJpaRepository.findById(id)
                .map(ProductMapper.Instance::toProduct)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long count() {
        return this.productJpaRepository.count();
    }

    @Override
    public void deleteAll() {
        this.productJpaRepository.deleteAll();
    }

    @Override
    public List<Product> findAll() {
        return this.productJpaRepository.findAll().stream().map(ProductMapper.Instance::toProduct).collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Product> products) {
        this.productJpaRepository.saveAll(products.stream().map(ProductMapper.Instance::toProductEntity).collect(Collectors.toList()));
    }

}
