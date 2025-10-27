package com.Developer.DreamShop.service.Cart;

import com.Developer.DreamShop.model.CartItem;
import com.Developer.DreamShop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartItemService   {
        void addItemToCart(Long id,long productId,int quantity);
        void removeItemFromCart(long id,long productId);
        void updateQuantity(long id,long productId,int quantity);
        CartItem getcartItem(long id, long productId);
}
