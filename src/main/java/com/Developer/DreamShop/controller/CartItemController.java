package com.Developer.DreamShop.controller;


import com.Developer.DreamShop.exceptions.ResourceNotFoundException;
import com.Developer.DreamShop.model.Cart;
import com.Developer.DreamShop.model.CartItem;
import com.Developer.DreamShop.model.User;
import com.Developer.DreamShop.response.ApiResponse;
import com.Developer.DreamShop.service.Cart.ICartItemService;
import com.Developer.DreamShop.service.Cart.ICartService;
import com.Developer.DreamShop.service.User.IUserService;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/CartItems")
public class CartItemController {
    private final IUserService userService;
    private final ICartItemService cartItemService;
    public final ICartService cartService;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse>addItemToCart(
                                                    @RequestParam long productId,
                                                    @RequestParam int quantity){

        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Successfully Added CartItem" ,null ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Resource not found" + e.getMessage() , null));
        }catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse("unauthorized" + e.getMessage() , null));
        }
    }



    @DeleteMapping("/{id}/item/{productId}/remove")
    public ResponseEntity<ApiResponse>removeItemFromCart(@PathVariable long id,@PathVariable long productId){
        try {
            cartItemService.removeItemFromCart(id, productId);
            return ResponseEntity.ok(new ApiResponse("Successfully removed  CartItem" ,null ));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse("Error Doesn't Exist" + e.getMessage() , null));
        }
    }

    @PutMapping("/{id}/item/{productId}/update")
    public ResponseEntity<ApiResponse>updateQuantity(@PathVariable long id,
                                                     @PathVariable long productId,
                                                     @RequestParam int quantity){
        try {
            cartItemService.updateQuantity(id, productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Successfully updated Quantity CartItem" ,null ));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse("Error Item doesn't Exist" + e.getMessage() , null));
        }
    }


//    public ResponseEntity<ApiResponse>getCartItem(long cartId, long productId){
//        try {
//            CartItem cartItem = cartItemService.getcartItem(cartId, productId);
//            return ResponseEntity.ok(new ApiResponse("Successfully get CartItem" , cartItem));
//        } catch (Exception e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error Item doesn't Exist" + e.getMessage() , null));
//
//        }
//
//    }


}
