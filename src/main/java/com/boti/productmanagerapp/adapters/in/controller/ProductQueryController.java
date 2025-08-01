package com.boti.productmanagerapp.adapters.in.controller;

import com.boti.productmanagerapp.adapters.in.web.response.ProductResponse;
import com.boti.productmanagerapp.application.core.domain.PageResult;
import com.boti.productmanagerapp.application.core.domain.Product;
import com.boti.productmanagerapp.application.core.usecases.ProductQueryUsecase;
import com.boti.productmanagerapp.utils.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/products")
public class ProductQueryController {

    private final ProductQueryUsecase productQueryService;

    @Autowired
    public ProductQueryController(ProductQueryUsecase productQueryService) {
        this.productQueryService = productQueryService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> queryProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        if (name == null && minPrice == null && maxPrice == null) {
            return ResponseEntity.badRequest().build();
        }

        PageResult<Product> results = productQueryService.execute(name, minPrice, maxPrice, pageable.getPageNumber(), pageable.getPageSize());

        List<ProductResponse> responseList = results.getContent().stream()
                .map(ProductMapper.Instance::toProductResponse)
                .collect(Collectors.toList());

        Page<ProductResponse> response = new PageImpl<>(responseList, pageable, results.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
