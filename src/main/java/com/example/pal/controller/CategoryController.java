package com.example.pal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import com.example.pal.dto.ResponseDTO;
import com.example.pal.model.Category;
import com.example.pal.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<Category>> createCategory(@RequestParam String name) {
        try {
            Category category = categoryService.createCategory(name);
            ResponseDTO<Category> response = new ResponseDTO<>("Category created successfully", category);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            ResponseDTO<Category> response = new ResponseDTO<>(e.getMessage(), null);
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new ResponseDTO<>("All categories fetched successfully", categories));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Category>> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        
        if (category.isPresent()) {
            return ResponseEntity.ok(new ResponseDTO<>("Category fetched successfully", category.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO<Category>> updateCategory(@PathVariable Long id, @RequestParam String name) {
        Category updatedCategory = categoryService.updateCategory(id, name);

        if (updatedCategory != null) {
            return ResponseEntity.ok(new ResponseDTO<>("Category updated successfully", updatedCategory));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
