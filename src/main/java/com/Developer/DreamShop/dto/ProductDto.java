package com.Developer.DreamShop.dto;

import com.Developer.DreamShop.model.Category;
import com.Developer.DreamShop.model.Image;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {

    private long id;
    private String name;
    private String brand;
    private BigDecimal productPrice;
    private int inventory;
    private String Description;


    private Category category;

    private List<ImageDto> images;
}
