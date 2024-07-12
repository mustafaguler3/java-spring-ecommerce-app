package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Basket;
import com.example.thymeleaf_demo.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket,Long> {
    Optional<Basket> findByUserId(Long userId);
    Basket findByUser(User user);
    Basket save(Basket basket);

    @Modifying
    @Transactional
    @Query("delete from BasketItem b where b.basket.id = :basketId")
    void deleteBasketByBasketId(@Param("basketId") Long basketId);
}
