package com.Developer.DreamShop.service.Cart;

import com.Developer.DreamShop.model.Cart;
import com.Developer.DreamShop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart (Long Id);
    void clearCart (Long Id);
    BigDecimal getTotalPrice(Long Id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
