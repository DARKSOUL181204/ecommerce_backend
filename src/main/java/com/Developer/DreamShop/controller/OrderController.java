package com.Developer.DreamShop.controller;


import com.Developer.DreamShop.dto.OrderDto;
import com.Developer.DreamShop.exceptions.ResourceNotFoundException;
import com.Developer.DreamShop.model.Order;
import com.Developer.DreamShop.response.ApiResponse;
import com.Developer.DreamShop.service.Order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/Orders")
public class OrderController {

    private final IOrderService orderService;



    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item Successfully Ordered " ,orderDto ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error order not placed " + e.getMessage() , null));
        }
    }

    @GetMapping("/{orderId}/orders")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId){
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order Found Successfully " ,order ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops !" + e.getMessage() , null));
        }
    }


    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrderDto> listOrders = orderService.getUserOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Order Found Successfully " ,listOrders ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops !" + e.getMessage() , null));
        }
    }






}
