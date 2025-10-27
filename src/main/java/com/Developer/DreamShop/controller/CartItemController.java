package com.Developer.DreamShop.controller;


import com.Developer.DreamShop.model.CartItem;
import com.Developer.DreamShop.response.ApiResponse;
import com.Developer.DreamShop.service.Cart.ICartItemService;
import com.Developer.DreamShop.service.Cart.ICartService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/CartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    public final ICartService cartService;

//    @PostMapping("/add")
//    public ResponseEntity<ApiResponse>addItemToCart(@RequestParam(required = false) Long id,
//                                                    @RequestParam long productId,
//                                                    @RequestParam int quantity){
//        try {
//            if(id == null|| id<=0){
//            id  = cartService.initializeNewCart();
//            }
//            cartItemService.addItemToCart(id,productId,quantity);
//            return ResponseEntity.ok(new ApiResponse("Successfully Added CartItem" ,null ));
//        } catch (Exception e) {
//            return ResponseEntity.status(CONFLICT).body(new ApiResponse("Error Already Exist" + e.getMessage() , null));
//        }
//    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestParam(required = false) Long id,
            @RequestParam long productId,
            @RequestParam int quantity) {

        try {
            // Step 1: Validate quantity
            if (quantity <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Quantity must be greater than zero", null));
            }

            // Step 2: Check / initialize cart
            try {
                if (id == null || id <= 0) {
                    id = cartService.initializeNewCart();
                    if (id == null) {
                        throw new RuntimeException("Cart initialization returned null");
                    }
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse("Error in cart initialization: " + e.getMessage(), null));
            }

            // Step 3: Add item to cart
            try {
                cartItemService.addItemToCart(id, productId, quantity);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse("Error in adding item to cart: " + e.getMessage(), null));
            }

            // Step 4: Success response
            return ResponseEntity.ok(new ApiResponse("Successfully Added CartItem", id));

        } catch (Exception e) {
            // Step 5: Catch any unexpected top-level exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexpected error: " + e.getMessage(), null));
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
