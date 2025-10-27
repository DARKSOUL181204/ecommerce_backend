package com.Developer.DreamShop.repository;

import com.Developer.DreamShop.model.Cart;
import com.Developer.DreamShop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository  extends JpaRepository<CartItem,Long > {

}
