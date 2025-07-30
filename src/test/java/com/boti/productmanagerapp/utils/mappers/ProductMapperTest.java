package com.boti.productmanagerapp.utils.mappers;

import com.boti.productmanagerapp.adapters.out.entities.ProductEntity;
import com.boti.productmanagerapp.application.core.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductMapper Tests")
class ProductMapperTest {

    @Test
    @DisplayName("Should map Product domain object to ProductEntity correctly")
    void shouldMapProductToProductEntityCorrectly() {
        // 1. Arrange (Preparar)
        Product product = new Product(
                1L,
                "Smartphone XYZ",
                100L,
                "$999.99",
                "Electronics",
                "Tech",
                "BR"
        );

        ProductEntity entity = ProductMapper.Instance.toProductEntity(product);

        assertNotNull(entity, "ProductEntity should not be null");
        assertEquals(product.getProductId(), entity.getId(), "Product ID should be mapped correctly");
        assertEquals(product.getProduct(), entity.getProduct(), "Product name should be mapped correctly");
        assertEquals(product.getQuantity(), entity.getQuantity(), "Quantity should be mapped correctly");
        assertEquals(0, product.getPrice().compareTo(entity.getPrice()), "Price (BigDecimal) should be mapped correctly");
        assertEquals(product.getType(), entity.getType(), "Type should be mapped correctly");
        assertEquals(product.getIndustry(), entity.getIndustry(), "Industry should be mapped correctly");
        assertEquals(product.getOrigin(), entity.getOrigin(), "Origin should be mapped correctly");
    }

    @Test
    @DisplayName("Should map ProductEntity to Product domain object correctly")
    void shouldMapProductEntityToProductCorrectly() {

        ProductEntity entity = new ProductEntity(
                1L,
                "RTIX",
                50L,
                "$1500.50",
                "Computers",
                "IT",
                "USA"
        );

        // 2. Act (Executar)
        Product product = ProductMapper.Instance.toProduct(entity);

        // 3. Assert (Verificar)
        assertNotNull(product, "Product should not be null");
        assertEquals(entity.getId(), product.getProductId(), "Product ID should be mapped correctly");
        assertEquals(entity.getProduct(), product.getProduct(), "Product name should be mapped correctly");
        assertEquals(entity.getQuantity(), product.getQuantity(), "Quantity should be mapped correctly");
        assertEquals(0, entity.getPrice().compareTo(product.getPrice()), "Price (BigDecimal) should be mapped correctly");
        assertEquals(entity.getType(), product.getType(), "Type should be mapped correctly");
        assertEquals(entity.getIndustry(), product.getIndustry(), "Industry should be mapped correctly");
        assertEquals(entity.getOrigin(), product.getOrigin(), "Origin should be mapped correctly");
    }

    @Test
    @DisplayName("Should return null when mapping a null Product to ProductEntity")
    void shouldReturnNullWhenMappingNullProductToEntity() {
        // 1. Arrange (Preparar) - Entrada nula
        Product product = null;

        // 2. Act (Executar)
        ProductEntity entity = ProductMapper.Instance.toProductEntity(product);

        // 3. Assert (Verificar)
        assertNull(entity, "ProductEntity should be null when input Product is null");
    }

    @Test
    @DisplayName("Should return null when mapping a null ProductEntity to Product")
    void shouldReturnNullWhenMappingNullEntityToProduct() {
        ProductEntity entity = null;
        Product product = ProductMapper.Instance.toProduct(entity);
        assertNull(product, "Product should be null when input ProductEntity is null");
    }

    @Test
    @DisplayName("Should update an existing ProductEntity from Product domain object correctly")
    void shouldUpdateProductEntityFromProductCorrectly() {
        Product product = new Product(
                1L,
                "Updated Widget",
                200L,
                "$50.75",
                "Tools",
                "Manufacturing",
                "Germany"
        );
        ProductEntity existingEntity = new ProductEntity(
                1L,
                "Old Widget",
                150L,
                "$40.00",
                "Old Type",
                "Old Industry",
                "Old Origin"
        );

        ProductMapper.Instance.updateProductEntityFromProduct(product, existingEntity);

        assertNotNull(existingEntity, "Existing entity should not be null after update");
        assertEquals(1L, existingEntity.getId(), "ProductId should remain unchanged after update");
        assertEquals(product.getProduct(), existingEntity.getProduct(), "Product name should be updated");
        assertEquals(product.getQuantity(), existingEntity.getQuantity(), "Quantity should be updated");
        assertEquals(0, product.getPrice().compareTo(existingEntity.getPrice()), "Price should be updated");
        assertEquals(product.getType(), existingEntity.getType(), "Type should be updated");
        assertEquals(product.getIndustry(), existingEntity.getIndustry(), "Industry should be updated");
        assertEquals(product.getOrigin(), existingEntity.getOrigin(), "Origin should be updated");
    }
}