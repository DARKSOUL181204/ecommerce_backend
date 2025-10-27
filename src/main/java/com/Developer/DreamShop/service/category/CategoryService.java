package com.Developer.DreamShop.service.category;

import com.Developer.DreamShop.exceptions.CategoryAlreadyExistException;
import com.Developer.DreamShop.exceptions.CategoryNotFoundException;
import com.Developer.DreamShop.model.Category;
import com.Developer.DreamShop.repository.CategoryRepository;
//import com.Developer.DreamShop.request.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
        .orElseThrow(()->new CategoryNotFoundException("Category Not Found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(category.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()->new CategoryAlreadyExistException(category.getName()+"Category already Exist"));
    }


    @Override
    public Category updateCategory(Category category, long id) {
        return categoryRepository.findById(id)
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                })
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }


    @Override
    public void deleteCategory(Long id) {
         categoryRepository.findById(id)
                 .ifPresentOrElse(categoryRepository::delete,
                         ()-> {
                             throw new CategoryNotFoundException("Category Not Found");
                         } );
    }
}




//
//    @Override
//    public Category updateCategory(CategoryUpdateRequest category,long id) {
//        return categoryRepository.findById(id).map(existingCategory -> updateExistingCategory(existingCategory,category))
//                .map(categoryRepository::save)
//                .orElseThrow(
//                        ()->new CategoryNotFoundException("Category not Found ")
//                );
//    }
//    Category updateExistingCategory(Category existingCategory, CategoryUpdateRequest category){
//        existingCategory.setName(category.getName());
//        return existingCategory;
//    }
