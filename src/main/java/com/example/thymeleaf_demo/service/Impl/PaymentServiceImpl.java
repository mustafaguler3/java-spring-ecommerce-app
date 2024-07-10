package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.Order;
import com.example.thymeleaf_demo.domain.Payment;
import com.example.thymeleaf_demo.repository.OrderRepository;
import com.example.thymeleaf_demo.repository.PaymentRepository;
import com.example.thymeleaf_demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Payment processPayment(Payment payment) {
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus("Success");

        paymentRepository.save(payment);

        Order order = payment.getOrder();
        order.setStatus("Paid");
        orderRepository.save(order);

        return payment;
    }
}


















