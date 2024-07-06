package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Cart;
import com.example.thymeleaf_demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserId(Long userId);

    Cart findByUser(User user);
}
