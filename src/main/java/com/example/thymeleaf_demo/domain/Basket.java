package com.example.thymeleaf_demo.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Basket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "basket",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<BasketItem> basketItems = new HashSet<BasketItem>();

    public BigDecimal getTotal(){
        return basketItems.stream()
                .map(BasketItem::getSubtotal)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}






















