package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {
    List<Review> findByProductId(int productId);
}
