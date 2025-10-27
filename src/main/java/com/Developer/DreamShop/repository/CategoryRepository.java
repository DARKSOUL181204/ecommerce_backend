package com.Developer.DreamShop.repository;

import com.Developer.DreamShop.model.Category;
import com.Developer.DreamShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long > {

    Category findByName(String name);

    boolean existsByName(String name);
}
