package com.Developer.DreamShop.dto;

import com.Developer.DreamShop.enums.OrderStatus;
import com.Developer.DreamShop.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
public class OrderDto {
    private Long Id;
    private LocalDate orderTime;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private Long userId;
    private List<OrderItemDto> items;
}
