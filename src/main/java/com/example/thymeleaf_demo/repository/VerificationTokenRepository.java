package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);
}
