package com.boti.productmanagerapp.adapters.in.controller;

import com.boti.productmanagerapp.adapters.in.web.dto.ProductRequest;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.core.usecases.InsertProduct;
import com.boti.productmanagerapp.utils.mappers.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private static final Logger log = LoggerFactory.getLogger(CreateProductController.class);
    private final InsertProduct insertProduct;

    @Autowired
    public CreateProductController(InsertProduct insertProduct) {
        this.insertProduct = insertProduct;
    }

    @PostMapping
    public ResponseEntity<?> execute(@RequestBody @Validated ProductRequest productRequest, UriComponentsBuilder uriBuilder) {
        log.info("Starting product insertion: {}", productRequest.getProduct());

        Product product = this.insertProduct.execute(ProductMapper.Instance.toProduct(productRequest));

        URI location = uriBuilder.path("/v1/products/{id}")
                .buildAndExpand(product.getProductId())
                .toUri();

        return ResponseEntity.created(location).body(ProductMapper.Instance.toProductResponse(product));
    }

}
