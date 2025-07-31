package com.boti.productmanagerapp.adapters.out.batchprocess;


import com.boti.productmanagerapp.application.core.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductWrapper {

    private List<Product> data;

}
