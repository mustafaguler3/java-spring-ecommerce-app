package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query("select c from Category c where c.id =:id")
    Category findById(@Param("id") Long id);

    @Query("select c from Category c where c.name =:name")
    Category findByName(@Param("name") String name);

    @Query(value = "SELECT c.id,c.name as category_name,p.name,p.brand\n" +
            "FROM category c\n" +
            "JOIN product p ON p.category_id = c.id;",nativeQuery = true)
    List<Category> findAllWithProducts();
    List<Category> findAll();
}
