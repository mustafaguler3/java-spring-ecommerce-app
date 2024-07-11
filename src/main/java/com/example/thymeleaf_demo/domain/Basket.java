package com.example.thymeleaf_demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<BasketItem> basketItems = new HashSet<BasketItem>();

    public BigDecimal getTotal(){
        return basketItems.stream()
                .map(BasketItem::getSubtotal)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}






















