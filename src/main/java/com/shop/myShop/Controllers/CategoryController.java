package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Category;
import com.shop.myShop.Entities.Picture;
import com.shop.myShop.Repositories.CategoryRepository;
import com.shop.myShop.Repositories.PictureRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("categories")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final PictureRepository pictureRepository;

    public CategoryController(CategoryRepository categoryRepository, PictureRepository pictureRepository) {
        this.categoryRepository = categoryRepository;
        this.pictureRepository = pictureRepository;
    }


    @GetMapping
    ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @PostMapping
    ResponseEntity<Category> addCategory(@RequestBody Category category) {
        category.getPicture().setCategory(category);
        Category c = categoryRepository.save(category);
        return ResponseEntity.ok(c);
    }


    @PutMapping("/{id}")
    ResponseEntity updateCategory(@RequestBody Category newCategory, @PathVariable Long id) {
        Map<String, String> error = new HashMap<>();
        Category c = categoryRepository.findById(id)
                .map(product -> {
                    product.setName(newCategory.getName());
                    Picture picture = pictureRepository.getPicturesByCategory(product.getId());
                    picture.setName(newCategory.getPicture().getName());
                    return categoryRepository.save(product);

                }).orElse(null);
        if (c != null)
            return ResponseEntity.ok(c);
        else {
            error.put("error", "Category not found");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteCategory(@PathVariable Long id) {
        Map<String, String> error = new HashMap<>();
        Category c = categoryRepository.findById(id).orElse(null);
        if (c != null) {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok(c);
        } else {
            error.put("error", "Category not found");
            return ResponseEntity.badRequest().body(error);
        }
    }
}
