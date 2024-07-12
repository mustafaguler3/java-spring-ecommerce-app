package com.example.thymeleaf_demo.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class BasketItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "basket_id")
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





















