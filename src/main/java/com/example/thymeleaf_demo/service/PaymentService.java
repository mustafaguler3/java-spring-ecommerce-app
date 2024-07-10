package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.Payment;

public interface PaymentService {
    Payment processPayment(Payment payment);
}
