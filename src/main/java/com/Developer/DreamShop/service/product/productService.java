package com.Developer.DreamShop.service.product;

import com.Developer.DreamShop.dto.ImageDto;
import com.Developer.DreamShop.dto.ProductDto;
import com.Developer.DreamShop.exceptions.ProductNotFoundException;
import com.Developer.DreamShop.model.Category;
import com.Developer.DreamShop.model.Image;
import com.Developer.DreamShop.model.Product;
import com.Developer.DreamShop.repository.ImageRepository;
import com.Developer.DreamShop.repository.ProductRepository;
import com.Developer.DreamShop.repository.CategoryRepository;
import com.Developer.DreamShop.request.AddProductRequest;
import com.Developer.DreamShop.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class productService implements IProductService{
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private  final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        // first check if the category is presented in db
        // if yes then add new product
        // if no then create new category and then add new product

//Optional.ofNullable is to handle null cases
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    // if null then create new newCategory
                    Category newCategory = new Category(request.getCategory().getName());
                    // save newCategory to repository
                    // spring jpa will save this to database
                    return categoryRepository.save(newCategory);
                });
            // set category of new product
        request.setCategory(category);
        // take all attributes of the saved category
        return productRepository.save(createProduct(request,category));
    }
    private Product createProduct(AddProductRequest request, Category category){
    return new Product(
            request.getProductName(),
            request.getProductBrand(),
            request.getProductPrice(),
            request.getInventory(),
            request.getDescription(),
            category
    );

    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById((long)id)
                .orElseThrow(()-> new ProductNotFoundException("Product not Found "));
    }


    @Override
    public void deleteProductById(long id) {
         productRepository.findById((long) id)
                 .ifPresentOrElse(productRepository::delete,
                         ()-> new ProductNotFoundException("Product not Found "));
    }

    @Override
    public Product updateProduct(ProductUpdateRequest product, long productid) {
        return productRepository.findById(productid)
                .map(existingproduct->updateExistingProduct(existingproduct,product))
                .map(productRepository::save)
                .orElseThrow(()->new ProductNotFoundException("Product not Found "));
    }
    Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){

        existingProduct.setName(request.getProductName());
        existingProduct.setBrand(request.getProductBrand());
        existingProduct.setProductPrice(request.getProductPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return  existingProduct;

    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String Brand) {
        return productRepository.findByBrand(Brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String Brand) {
        return productRepository.findByCategoryNameAndName(category,Brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByNameAndBrand(String name, String brand) {
        return productRepository.findByNameAndBrand(name,brand);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }


    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToProductDto).toList();

    }


    @Override
    public ProductDto convertToProductDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId((long)product.getId());
        List<ImageDto> imageDtos = images.stream().map(image -> modelMapper
                .map(image, ImageDto.class))
                .toList();

        productDto.setImages(imageDtos);
        return  productDto;

    }


}
