package com.example.thymeleaf_demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private Double discountAmount;
    private LocalDateTime expiryDate;

    public Coupon() {}

    public boolean isValid(){
        return expiryDate.isAfter(LocalDateTime.now());
    }

}






















