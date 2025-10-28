package com.Developer.DreamShop.dto;

import com.Developer.DreamShop.model.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;

}
