package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query("select c from Category c where c.id =:id")
    Category findById(@Param("id") Long id);
}
