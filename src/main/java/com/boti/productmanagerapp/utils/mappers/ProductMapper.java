package com.boti.productmanagerapp.utils.mappers;

import com.boti.productmanagerapp.adapters.in.web.dto.ProductRequest;
import com.boti.productmanagerapp.adapters.in.web.response.ProductResponse;
import com.boti.productmanagerapp.adapters.out.entities.ProductEntity;
import com.boti.productmanagerapp.application.core.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper Instance = Mappers.getMapper(ProductMapper.class);

    @Mapping(source ="productId", target = "id" )
    ProductEntity toProductEntity(Product product);

    @Mapping(source ="id", target = "productId" )
    Product toProduct(ProductEntity productEntity);

    @Mapping(target = "id", ignore = true)
    void updateProductEntityFromProduct(Product product, @MappingTarget ProductEntity entity);

    @Mapping(target = "productId", ignore = true)
    Product toProduct(ProductRequest productRequest);

    @Mapping(source ="productId", target = "id" )
    ProductResponse toProductResponse(Product product);

    ProductResponse toProductResponse(ProductEntity productEntity);

}
