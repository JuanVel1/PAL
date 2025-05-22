package com.example.pal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pal.dto.CreateEnrollmentDTO;
import com.example.pal.dto.ResponseDTO;
import com.example.pal.model.Course;
import com.example.pal.model.Enrollment;
import com.example.pal.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "http://localhost:3000")
public class EnrollmentController {
    
    @Autowired
    private EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCourse(@RequestBody CreateEnrollmentDTO createEnrollmentDTO) {
        try {
            Enrollment enrollment = enrollmentService.createEnrollment(createEnrollmentDTO);
            ResponseDTO<Enrollment> response = new ResponseDTO<>("Course registered successfully", enrollment);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            ResponseDTO<Course> response = new ResponseDTO<>(e.getMessage() + " ", null);
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/my-courses")
    public ResponseEntity<?> getEnrollments(@RequestParam Long userId) {
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUserId(userId);
            ResponseDTO<Enrollment> response = new ResponseDTO<>("Enrollments retrieved successfully", enrollments);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
