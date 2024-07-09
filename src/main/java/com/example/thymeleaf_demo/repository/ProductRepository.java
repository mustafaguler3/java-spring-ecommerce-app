package com.example.thymeleaf_demo.repository;

import com.example.thymeleaf_demo.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("select p from Product p where p.id=:x")
    Product findProductById(@Param("x") Long id);

    Page<Product> findAll(Pageable pageable);

    /*@Query(value = "SELECT distinct count(*) ,p.brand FROM new.product p\n" +
            "group by p.brand",nativeQuery = true)
    List<String> getProductsByBrand(); */
}





















