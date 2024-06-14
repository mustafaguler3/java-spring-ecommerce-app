package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Page<User> findAll(Pageable pageable);

    User findByUsername(String username);
    User findByEmail(String email);

    @Query("select u from User u where u.username LIKE %:keyword%")
    List<User> searchByUsername(@Param("keyword") String keyword);

}























