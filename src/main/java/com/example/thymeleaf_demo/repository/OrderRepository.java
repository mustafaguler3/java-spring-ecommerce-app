package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT o FROM Order o where o.user.id = :userId")
    List<Order> findOrderByUserId(@Param("userId") Long userId);
}
