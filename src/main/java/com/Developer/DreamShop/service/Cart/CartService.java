package com.Developer.DreamShop.service.Cart;

import com.Developer.DreamShop.exceptions.ResourceNotFoundException;
import com.Developer.DreamShop.model.Cart;
import com.Developer.DreamShop.model.CartItem;
import com.Developer.DreamShop.model.User;
import com.Developer.DreamShop.repository.CartItemRepository;
import com.Developer.DreamShop.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@Service
@RequiredArgsConstructor
public class CartService implements ICartService{


    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);


    @Override
    public Cart getCart(Long Id) {
        Cart cart = cartRepository.findById(Id)
                .orElseThrow(()-> {
                    return new ResourceNotFoundException("Resource not Found");
                });
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long Id) {
        Cart cart = getCart(Id);
        cartRepository.deleteAllById(Id);
        cart.getCartItems().clear();
        cartRepository.deleteById(Id);
    }

    @Override
    public BigDecimal getTotalPrice(Long Id) {
        Cart cart = getCart(Id);
        return cart.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
    }


@Override
public Cart initializeNewCart(User user) {
   return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(()->{
       Cart cart = new Cart();
       cart.setUser(user);
       return cartRepository.save(cart);
   });
}



    @Override
    public Cart getCartByUserId(Long userId) {

        return cartRepository.findByUserId(userId);
    }

}
