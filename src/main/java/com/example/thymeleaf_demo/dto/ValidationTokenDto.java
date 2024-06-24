package com.example.thymeleaf_demo.dto;

import com.example.thymeleaf_demo.domain.User;
import jakarta.persistence.*;

import java.util.Date;

public class ValidationTokenDto {

    private String token;
    private UserDto user;
    private Date expiryDate;
}
