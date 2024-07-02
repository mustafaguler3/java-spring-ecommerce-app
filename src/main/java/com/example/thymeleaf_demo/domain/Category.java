package com.example.thymeleaf_demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<Product>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }
}

























