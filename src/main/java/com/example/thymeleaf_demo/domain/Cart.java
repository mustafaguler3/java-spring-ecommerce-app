package com.example.thymeleaf_demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<CartItem> cartItems = new HashSet<CartItem>();
}






















