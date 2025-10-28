package com.Developer.DreamShop.service.Order;

import com.Developer.DreamShop.dto.OrderDto;
import com.Developer.DreamShop.enums.OrderStatus;
import com.Developer.DreamShop.exceptions.ResourceNotFoundException;
import com.Developer.DreamShop.model.*;
import com.Developer.DreamShop.repository.OrderRepository;
import com.Developer.DreamShop.repository.ProductRepository;
import com.Developer.DreamShop.service.Cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class orderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    private  final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> listOrderItems = createOrderItems(order,cart);

        order.setOrderItems(new HashSet<>(listOrderItems));
        order.setTotalAmount(calculateTotalAmount(listOrderItems));
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return savedOrder;
    }


    private Order createOrder(Cart cart){
        Order order = new Order();
        // set user
        order.setUser(cart.getUser());
        // set order
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderTime(LocalDate.now());
        return order;
    }



    private List<OrderItem> createOrderItems (Order order, Cart cart){
        return  cart.getCartItems().stream().map(cartItem -> {
            // manage inventory
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return  new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }



    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
    return orderItemList
            .stream()
            .map(item->item.getPrice()
                    .multiply(new BigDecimal(item.getQuantity())))
            .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        /* get orderId */
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException(" No Order  Found "));
    }

    @Override
    public List<OrderDto> getUserOrder(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order){
        return  modelMapper.map(order,OrderDto.class);
    }

}
