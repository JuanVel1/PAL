package com.example.pal.service;

import com.example.pal.model.Category;
import com.example.pal.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(String name) {
        //Verificar si la categoría ya existe en la base de datos antes de crearla.
        //Si la categoría ya existe, debe devolver un mensaje de error que diga "Category already exists".
        //Si la categoría no existe, debe crearla y devolver un mensaje de éxito que diga "Category created successfully".
        //El método debe devolver un objeto de tipo Category.
        if (categoryRepository.findByName(name) != null) {
            throw new RuntimeException("Category already exists");
        }

        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category updateCategory(Long id, String name) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found!"));
        category.setName(name);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
