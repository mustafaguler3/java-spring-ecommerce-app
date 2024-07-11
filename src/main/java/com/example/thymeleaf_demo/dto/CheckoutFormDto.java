package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.Address;
import com.example.thymeleaf_demo.domain.Payment;
import com.example.thymeleaf_demo.enums.PaymentMethodType;
import lombok.Data;

@Data
public class CheckoutFormDto {
    private Address address;
    private Payment payment;
    private PaymentMethodType paymentMethod;
}
