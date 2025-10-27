package com.Developer.DreamShop.request;

import com.Developer.DreamShop.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class CategoryUpdateRequest {
    private String Name;
    private List<Product> products;
}
