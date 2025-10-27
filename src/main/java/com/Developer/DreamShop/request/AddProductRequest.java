package com.Developer.DreamShop.request;

import com.Developer.DreamShop.model.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class AddProductRequest {
    private long id;
    private String productName;
    private String productBrand;
    private BigDecimal productPrice;
    private int inventory;
    private String Description;
    private Category category;
}
