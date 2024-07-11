package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Basket;
import com.example.thymeleaf_demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Basket,Long> {
    Optional<Basket> findByUserId(Long userId);

    Basket findByUser(User user);
}
