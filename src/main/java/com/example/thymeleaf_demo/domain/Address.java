package com.example.thymeleaf_demo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    private String firstName;
    private String lastName;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
























