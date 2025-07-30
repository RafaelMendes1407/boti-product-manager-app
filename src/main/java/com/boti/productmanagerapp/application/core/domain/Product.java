package com.boti.productmanagerapp.application.core.domain;

import java.math.BigDecimal;

public class Product {

    private long productId;
    private String product;
    private long quantity;
    private String price;
    private String type;
    private String industry;
    private String origin;

    public Product(long productId, String product, long quantity, String price, String type, String industry, String origin) {
        this.productId = productId;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.industry = industry;
        this.origin = origin;
    }

    public void setProductId(long id) {
        this.productId = id;
    }

    public long getProductId() {
        return this.productId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public BigDecimal getTotalValue() {
        price = this.getPrice().replace("$", "");
        return new BigDecimal(price).multiply(BigDecimal.valueOf(this.getQuantity()));
    }

}
