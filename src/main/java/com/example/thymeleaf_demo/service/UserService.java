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
    void saveUser(UserDto user) throws Exception;
    void deleteUser(User user);
    void updateUser(User user);
    UserDto findByUsername(String username) throws Exception;
    UserDto findByEmail(String email) throws Exception;
    boolean verifyUser(String token);

    Optional<User> findById(Integer id);

    List<User> searchByUsername(String keyword);
    Page<User> findAll(PageRequest pageRequest);

    String createPasswordResetToken(UserDto user);
    boolean validatePasswordResetToken(String token);
    UserDto getUserByPasswordResetToken(String token);
    void updatePassword(UserDto user,String newPassword);
}






















