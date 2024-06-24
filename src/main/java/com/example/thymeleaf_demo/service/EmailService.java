package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationEmail(User user, String verificationLink);
    void sendPasswordResetEmail(User user, String resetLink);
}
