package com.boti.productmanagerapp.adapters.in.controller;

import com.boti.productmanagerapp.adapters.in.web.response.ProductResponse;
import com.boti.productmanagerapp.application.core.usecases.FindProductByIdUsecase;
import com.boti.productmanagerapp.utils.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
public class FindProductByIdController {

    private final FindProductByIdUsecase findProductByIdUsecase;

    @Autowired
    public FindProductByIdController(FindProductByIdUsecase findProductByIdUsecase) {
        this.findProductByIdUsecase = findProductByIdUsecase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(ProductMapper.Instance.toProductResponse(findProductByIdUsecase.execute(id)));
    }

}
