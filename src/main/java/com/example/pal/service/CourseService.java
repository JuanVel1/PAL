package com.example.pal.service;

import com.example.pal.dto.CreateCurseDTO;
import com.example.pal.model.Course;
import com.example.pal.repository.CategoryRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository CourseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public Course createCourse(CreateCurseDTO createCurseDTO) {

        // Validate that the category exists
        if (categoryRepository.findByName(createCurseDTO.getCategory()) == null) {
            throw new RuntimeException("Category not found!");
        }
        // Validate that the instructor exists
        if (!userRepository.existsById(createCurseDTO.getInstructorId())) {
            throw new RuntimeException("Instructor not found!");
        }
        // Validate that the course title is not empty
        if (createCurseDTO.getTitle() == null || createCurseDTO.getTitle().isEmpty()) {
            throw new RuntimeException("Course title cannot be empty!");
        }
        // Validate that the course description is not empty
        if (createCurseDTO.getDescription() == null || createCurseDTO.getDescription().isEmpty()) {
            throw new RuntimeException("Course description cannot be empty!");
        }
        // Validate that the course price is not negative
        if (createCurseDTO.getPrice() < 0) {
            throw new RuntimeException("Course price cannot be negative!");
        }
        // Validate that the course status is not empty
        if (createCurseDTO.getStatus() == null || createCurseDTO.getStatus().isEmpty()) {
            throw new RuntimeException("Course status cannot be empty!");
        }
        // Validate that the course average grade is not negative
        if (createCurseDTO.getAverage_grade() < 0) {
            throw new RuntimeException("Course average grade cannot be negative!");
        }
        // Validate that the course difficulty level is not empty
        if (createCurseDTO.getDifficultyLevel() == null || createCurseDTO.getDifficultyLevel().isEmpty()) {
            throw new RuntimeException("Course difficulty level cannot be empty!");
        }
        // Validate that the course difficulty level is in BEGINNER, INTERMEDIATE, ADVANCED, ALL
        if (!createCurseDTO.getDifficultyLevel().equals("BEGINNER") &&
                !createCurseDTO.getDifficultyLevel().equals("INTERMEDIATE") &&
                !createCurseDTO.getDifficultyLevel().equals("ADVANCED") &&
                !createCurseDTO.getDifficultyLevel().equals("ALL")) {
            throw new RuntimeException("Course difficulty level must be BEGINNER, INTERMEDIATE, ADVANCED or ALL!");
        }

        Course Course = new Course();
        Course.setTitle(createCurseDTO.getTitle());
        Course.setCategory(categoryRepository.findByName(createCurseDTO.getCategory()));
        Course.setInstructor(userRepository.findById(createCurseDTO.getInstructorId()).get());
        Course.setDescription(createCurseDTO.getDescription());
        Course.setPrice(createCurseDTO.getPrice());
        Course.setStatus(createCurseDTO.getStatus());
        Course.setAverage_grade(createCurseDTO.getAverage_grade());
        Course.setDifficultyLevel(createCurseDTO.getDifficultyLevel());
        Course.setPublicationDate(createCurseDTO.getPublicationDate());
        Course.setDurationInHours(createCurseDTO.getDurationInHours());

        return CourseRepository.save(Course);
    }

    public List<Course> getAllCategories() {
        return CourseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return CourseRepository.findById(id);
    }

    public List<Course> getCoursesByCategory(String categoryName) {
        return CourseRepository.findByCategoryName(categoryName);
    }

    public Course updateCourse(Long id, String name) {
        Course Course = CourseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found!"));
        Course.setTitle(name);
        return CourseRepository.save(Course);
    }

    public void deleteCourse(Long id) {
        CourseRepository.deleteById(id);
    }
}
