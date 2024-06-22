package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordResetTokenDto {

    private Long id;
    private String token;
    private UserDto userDto;
    private LocalDateTime expiryDate;


    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
