package com.boti.productmanagerapp.adapters.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private long id;
    private String product;
    private String type;
    private String price;
    private long quantity;
    private String industry;
    private String origin;


}
