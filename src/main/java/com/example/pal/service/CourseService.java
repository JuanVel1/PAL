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

        Course Course = new Course();
        Course.setTitle(createCurseDTO.getTitle());
        Course.setCategory(categoryRepository.findByName(createCurseDTO.getCategory()));
        Course.setInstructor(userRepository.findById(createCurseDTO.getInstructorId()).get());
        Course.setDescription(createCurseDTO.getDescription());
        Course.setPrice(createCurseDTO.getPrice());
        Course.setStatus(createCurseDTO.getStatus());
        Course.setAverage_grade(createCurseDTO.getAverage_grade());

        return CourseRepository.save(Course);
    }

    public List<Course> getAllCategories() {
        return CourseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return CourseRepository.findById(id);
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
