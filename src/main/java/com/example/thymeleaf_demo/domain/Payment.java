package com.example.thymeleaf_demo.domain;

import com.example.thymeleaf_demo.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private String currency;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentDate;

    // Credit Card details
    private String cardNumber;
    private String nameOnCard;
    private String cardExpiryDate;
    private String cardCVV;

    // PayPal details
    private String paypalEmail;

}
























