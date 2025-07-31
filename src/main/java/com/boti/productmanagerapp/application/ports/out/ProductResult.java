package com.boti.productmanagerapp.application.ports.out;

import com.boti.productmanagerapp.application.core.domain.Product;

public interface ProductResult {

    Product getProduct();
    Exception getException();
    boolean hasError();

}
