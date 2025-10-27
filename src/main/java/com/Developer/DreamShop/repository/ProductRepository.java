package com.Developer.DreamShop.repository;

import com.Developer.DreamShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long > {
     List<Product> findByCategoryName(String category);
     List<Product> findByBrand(String brand);
     List<Product> findByCategoryNameAndName(String category, String brand);

    List<Product> findByName(String name);
    List<Product> findByNameAndBrand(String name, String brand);

    Long countByBrandAndName(String brand, String name);
}
