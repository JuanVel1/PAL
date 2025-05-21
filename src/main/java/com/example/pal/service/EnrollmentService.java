package com.example.pal.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pal.dto.CreateEnrollmentDTO;
import com.example.pal.model.Course;
import com.example.pal.model.Enrollment;
import com.example.pal.model.Payment;
import com.example.pal.model.User;
import com.example.pal.repository.EnrollmentRepository;

@Service
public class EnrollmentService {
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private PaymentService paymentService;

    public Enrollment createEnrollment(CreateEnrollmentDTO createEnrollementDTO) {
        // Validate that the user exists
        if (!userService.getUserById(createEnrollementDTO.getUserId()).isPresent()) {
            throw new RuntimeException("User not found!");
        }
        // Validate that the course exists
        if (!courseService.getCourseById(createEnrollementDTO.getCourseId()).isPresent()){
            throw new RuntimeException("Course not found!");
        }
        
        User user = userService.getUserById(createEnrollementDTO.getUserId()).get();
        //obtener el curso
        Course course = courseService.getCourseById(createEnrollementDTO.getCourseId()).get();
        
        // Validate that the user is not already enrolled in the course
        if (enrollmentRepository.findByUserId(user.getId()).stream()
                .anyMatch(enrollment -> enrollment.getCourse().getId().equals(course.getId()))) {
            throw new RuntimeException("User is already enrolled in this course!");
        }
        // Check if the course is paid
        if (course.getPrice() > 0) {
            
            if (createEnrollementDTO.getPaymentId() == null) {
                throw new RuntimeException("Payment ID cannot be null for paid courses!");
            }
            //  Validate that the payment exists
            if (!paymentService.getPaymentById(createEnrollementDTO.getPaymentId()).isPresent()) {
                throw new RuntimeException("Payment not found!");
            }
            // Validate that the payment is associated with the user
            Payment payment = paymentService.getPaymentById(createEnrollementDTO.getPaymentId()).get();
            if (!payment.getUser().equals(user)) {
                throw new RuntimeException("Payment does not belong to the user!");
            }
            // Validate that the payment amount matches the course price
            if (payment.getAmount() != course.getPrice()) {
                throw new RuntimeException("Payment amount does not match course price!");
            }
        } 
        
        // Create and save the enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsByUserId(Long userId) {
        // Validate that the user exists
        if (!userService.getUserById(userId).isPresent()) {
            throw new RuntimeException("User not found!");
        }
        return enrollmentRepository.findByUserId(userId);
    }
}
