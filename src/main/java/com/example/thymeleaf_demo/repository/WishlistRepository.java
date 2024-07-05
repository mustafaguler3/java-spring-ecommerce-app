package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Product;
import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.domain.Wishlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    List<Wishlist> findByUserId(Long userId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    Wishlist findByUserIdAndProductId(Long userId, Long productId);

    @Modifying
    @Transactional
    @Query(value = "delete from wishlist w where w.product_id =:productId",nativeQuery = true)
    void deleteByProductId(@Param("productId") Long productId);

    Optional<Wishlist> findByUserAndProduct(User user, Product product);
}
