package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Category;
import com.shop.myShop.Repositories.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
@CrossOrigin(origins = "http://localhost:4200",maxAge = 3600)
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @GetMapping
    List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    Category addCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }


    @PutMapping("/{id}")
    Category updateCategory(@RequestBody Category newCategory, @PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(product -> {
                    product.setName(newCategory.getName());
                    return categoryRepository.save(product);
                }).get();
    }
}
