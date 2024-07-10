package com.example.thymeleaf_demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "'order'")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
    private String status;
    private LocalDateTime orderDate;

    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL)
    private Payment payment;

    public Double getTotalAmount(){
        return orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}



















