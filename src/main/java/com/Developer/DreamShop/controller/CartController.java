package com.Developer.DreamShop.controller;


import com.Developer.DreamShop.exceptions.ResourceNotFoundException;
import com.Developer.DreamShop.model.Cart;
import com.Developer.DreamShop.response.ApiResponse;
import com.Developer.DreamShop.service.Cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/Cart")
public class CartController {

    private final ICartService cartService;

    @GetMapping("/{Id}/my-cart")
    public ResponseEntity<ApiResponse> getCart ( @PathVariable Long Id){
        try {
            Cart cart =cartService.getCart(Id);
            return ResponseEntity.ok(new ApiResponse("Successfully Get Cart" ,cart ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error Cart not Found " + e.getMessage() , null));
        }
    }

    @DeleteMapping("/{Id}/clear-cart")
    public ResponseEntity<ApiResponse> clearCart (@PathVariable Long Id){
        try {
            cartService.clearCart(Id);
            return ResponseEntity.ok(new ApiResponse("Successfully Clear Cart" ,null ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error Cart not Found unable to clear " + e.getMessage() , null));
        }
    }


    @PutMapping("/{Id}/cart/total-price")
    public ResponseEntity<ApiResponse>  getTotalPrice(@PathVariable Long Id){
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(Id);
            return ResponseEntity.ok(new ApiResponse("Successfully Get total price" ,totalPrice ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error Cart Total Amount not Found " + e.getMessage() , null));
        }
    }




}
