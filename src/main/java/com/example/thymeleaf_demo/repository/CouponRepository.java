package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> {
    @Query(value = "select * from coupon",nativeQuery = true)
    Coupon findByCode(String couponCode);
}
