package com.boti.productmanagerapp.adapters.in.controller;

import com.boti.productmanagerapp.adapters.in.web.dto.ProductRequest;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.usecases.InsertProductUsecase;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import com.boti.productmanagerapp.infrastructure.LoggerAdapter;
import com.boti.productmanagerapp.utils.mappers.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/products")
public class CreateProductController {

    private final LoggerPort log;
    private final InsertProductUsecase insertProductUsecase;

    @Autowired
    public CreateProductController(LoggerPort log, InsertProductUsecase insertProductUsecase) {
        this.log = log;
        this.insertProductUsecase = insertProductUsecase;
    }

    @PostMapping
    public ResponseEntity<?> execute(@RequestBody @Validated ProductRequest productRequest, UriComponentsBuilder uriBuilder) {
        log.info(CreateProductController.class, String.format("Starting product insertion: %s", productRequest.getProduct()));

        Product product = this.insertProductUsecase.execute(ProductMapper.Instance.toProduct(productRequest));

        URI location = uriBuilder.path("/v1/products/{id}")
                .buildAndExpand(product.getProductId())
                .toUri();

        return ResponseEntity.created(location).body(ProductMapper.Instance.toProductResponse(product));
    }

}
