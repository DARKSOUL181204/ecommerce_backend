package com.Developer.DreamShop.request;

import com.Developer.DreamShop.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    private long id;
    private String productName;
    private String productBrand;
    private BigDecimal productPrice;
    private int inventory;
    private String Description;
    private Category category;
}
