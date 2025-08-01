package com.boti.productmanagerapp.adapters.out.entities;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = {"product", "type"}))
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String product;
    private long quantity;
    private BigDecimal price;
    private String type;
    private String industry;
    private String origin;

    public void setPrice(String price) {
        this.price = new BigDecimal(price.replace("$", ""));
    }
}
