package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.RegisterDto;
import com.example.thymeleaf_demo.dto.UserDto;
import jakarta.mail.MessagingException;

public interface UserService {
    void saveUser(UserDto user) throws MessagingException;
    void deleteUser(User user);
    void updateUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean verifyUser(String token);
}






















