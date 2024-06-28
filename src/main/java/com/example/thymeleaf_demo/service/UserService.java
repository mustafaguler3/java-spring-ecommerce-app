package com.example.thymeleaf_demo.service;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.PasswordResetTokenDto;
import com.example.thymeleaf_demo.dto.RegisterDto;
import com.example.thymeleaf_demo.dto.UserDto;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserDto userDto);
    void deleteUser(UserDto user);
    void updateUser(UserDto user);

    UserDto findByUsername(String username);
    UserDto findByEmail(String email);
    boolean verifyUser(String token);

    UserDto findById(int id);
    List<User> searchByUsername(String keyword);
    Page<User> findAll(PageRequest pageRequest);

    String createPasswordResetToken(User user);
    boolean validatePasswordResetToken(String token);
    User getUserByPasswordResetToken(String token);
    void updatePassword(User user,String newPassword);
}






















