package com.Developer.DreamShop.repository;

import com.Developer.DreamShop.model.Cart;
import com.Developer.DreamShop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart,Long > {

    void deleteAllById(Long id);

}
