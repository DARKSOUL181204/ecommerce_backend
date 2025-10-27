package com.Developer.DreamShop.service.Cart;

import com.Developer.DreamShop.exceptions.ResourceNotFoundException;
import com.Developer.DreamShop.model.Cart;
import com.Developer.DreamShop.model.CartItem;
import com.Developer.DreamShop.model.Product;
import com.Developer.DreamShop.repository.CartItemRepository;
import com.Developer.DreamShop.repository.CartRepository;
import com.Developer.DreamShop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartRepository cartRepository;
    private final ICartService cartService;
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;

    @Override
    public void addItemToCart(Long id, long productId, int quantity) {

        Cart cart = cartService.getCart(id);
        Product product = productService.getProductById(productId);
        CartItem cartItem  = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst().orElse(new CartItem());

        if(cartItem.getId() == null){
                    cartItem.setCart(cart);
                    cartItem.setProduct(product);
                    cartItem.setQuantity(quantity);
                    cartItem.setUnitPrice(product.getProductPrice());
                }
        else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(long id, long productId) {
        Cart cart = cartService.getCart(id);
        Product product = productService.getProductById(productId);
        CartItem itemToRemove  = getcartItem(id, product.getId());
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            cartRepository.delete(cart);
            System.out.println("Cart deleted because it became empty");
        }

    }





    @Override
    public void updateQuantity(long id, long productId, int quantity) {
        Cart cart = cartService.getCart(id);
        Product product = productService.getProductById(productId);
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId()== productId)
                .findFirst()
                .ifPresent(item-> {
                            item.setQuantity(quantity);
                            item.setTotalPrice(item.getProduct().getProductPrice());
                            item.setTotalPrice();
                        });
        BigDecimal totalAmount = cart.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);

    }

    @Override
    public CartItem getcartItem(long id, long productId){
        Cart cart = cartService.getCart(id);
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId()== productId)
                .findFirst().orElseThrow(()-> new ResourceNotFoundException("Product not Found "));
    }
}
