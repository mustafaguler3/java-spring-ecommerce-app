package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.PasswordResetToken;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);
    void deleteByUser(User user);
    PasswordResetToken findByUser(User user);
}
