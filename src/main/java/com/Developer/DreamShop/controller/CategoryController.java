package com.Developer.DreamShop.controller;

import com.Developer.DreamShop.exceptions.CategoryAlreadyExistException;
import com.Developer.DreamShop.model.Category;
import com.Developer.DreamShop.response.ApiResponse;
import com.Developer.DreamShop.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class  CategoryController {

    private final ICategoryService categoryService;



    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories =  categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Successfully Got Categories" ,categories ));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Server Error " + e.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse>  addCategory(@RequestParam Category category){
        try {
            Category newCategory =  categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Successfully Added Category" ,newCategory ));
        } catch (CategoryAlreadyExistException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse("Error Already Exist" + e.getMessage() , null));
        }
    }



    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable long id){
        try {
            Category category =  categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Successfully Got Category" ,category ));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Category Id Not Found  " + e.getMessage(), NOT_FOUND));
        }

    }


    @GetMapping("/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category category =  categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Successfully Got Category" ,category ));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Category Name Not Found  " + e.getMessage(), NOT_FOUND));
        }

    }


    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(Long id){
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted Category" ,null ));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Category Not Found  " + e.getMessage(), NOT_FOUND));
        }

    }


    @PostMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category,@PathVariable long id ){
        try {
            Category theCategory =  categoryService.getCategoryById(id);
            if (theCategory!= null){
                Category updatedCategory= categoryService.updateCategory(theCategory, theCategory.getId());
            return ResponseEntity.ok(new ApiResponse("Successfully deleted Category" ,null ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(NOT_IMPLEMENTED)
                    .body(new ApiResponse("Category Unable to update " + e.getMessage(), NOT_IMPLEMENTED));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Server Error while Update Category  "  , INTERNAL_SERVER_ERROR));

    }




}
