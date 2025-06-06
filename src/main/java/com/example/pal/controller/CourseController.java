package com.example.pal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import com.example.pal.dto.CreateCurseDTO;
import com.example.pal.dto.ResponseDTO;
import com.example.pal.model.Course;
import com.example.pal.service.CourseService;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {

    @Autowired
    private CourseService CourseService;


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<Course>> createCourse(@RequestBody CreateCurseDTO createCurseDTO) 
    {
        try 
        {
            Course Course = CourseService.createCourse(createCurseDTO);
            ResponseDTO<Course> response = new ResponseDTO<>("Course created successfully", Course);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            ResponseDTO<Course> response = new ResponseDTO<>(e.getMessage() + " ", null);
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<Course>> getAllCategories() {
        List<Course> courses = CourseService.getAllCategories();
        ResponseDTO<Course> response = new ResponseDTO<>("Courses retrieved successfully", courses);
        return ResponseEntity.status(200).body(response);
    }
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ResponseDTO<Course>> getCoursesByCategory(@PathVariable String categoryName) {
        List<Course> courses = CourseService.getCoursesByCategory(categoryName);
        ResponseDTO<Course> response = new ResponseDTO<>("Courses retrieved successfully", courses);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Course>> getCourseById(@PathVariable Long id) {
        //Si el curso no existe, retornar un 404 sino retornar el curso a través del ResponseDTO
        Optional<Course> course = CourseService.getCourseById(id);
        if (course.isEmpty()) {
            ResponseDTO<Course> response = new ResponseDTO<>("Course not found", null);
            return ResponseEntity.status(404).body(response);
        }
        ResponseDTO<Course> response = new ResponseDTO<>("Course retrieved successfully", course.get());
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO<Course>> updateCourse(@PathVariable Long id, @RequestParam String title) {
        Course updatedCourse = CourseService.updateCourse(id, title);
        ResponseDTO<Course> response = new ResponseDTO<>("Course updated successfully", updatedCourse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        CourseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
