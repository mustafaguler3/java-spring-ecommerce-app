package com.example.thymeleaf_demo.domain;

import com.example.thymeleaf_demo.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amount;
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
























