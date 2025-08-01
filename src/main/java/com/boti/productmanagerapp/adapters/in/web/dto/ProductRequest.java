package com.boti.productmanagerapp.adapters.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product identifier cannot be empty")
    private String product;
    @NotBlank(message = "Type cannot be empty")
    private String type;
    @NotNull(message = "Price cannot be null")
    // TODO - criar validação para o preço
    private String price;
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Long quantity;
    private String industry;
    private String origin;
}