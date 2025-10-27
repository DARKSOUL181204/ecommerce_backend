package com.Developer.DreamShop.service.product;

import com.Developer.DreamShop.dto.ProductDto;
import com.Developer.DreamShop.model.Product;
import com.Developer.DreamShop.request.AddProductRequest;
import com.Developer.DreamShop.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);
    List<Product> getAllProduct();
    Product getProductById(long id);

    void deleteProductById(long id);
    Product updateProduct(ProductUpdateRequest product, long productid);
    List<Product> getProductByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String category,String Brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByNameAndBrand(String name,String Brand);
    Long countProductByBrandAndName(String brand,String name);


    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToProductDto(Product product);
}
