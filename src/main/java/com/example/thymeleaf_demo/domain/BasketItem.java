package com.example.thymeleaf_demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class BasketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Basket basket;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity;

    /*public double getSubtotal(){
        return this.quantity * this.product.getPrice();
    }*/

    public BigDecimal getSubtotal(){
        return product.getPrice().multiply(new BigDecimal(quantity));
    }
}





















