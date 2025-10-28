package com.Developer.DreamShop.service.Order;

import com.Developer.DreamShop.dto.OrderDto;
import com.Developer.DreamShop.model.Order;

import java.util.List;

public interface IOrderService  {

    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrder(Long userId);

    OrderDto convertToDto(Order order);
}

