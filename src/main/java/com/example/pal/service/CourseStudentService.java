package com.example.pal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.pal.repository.CourseStudentRepository;
import com.example.pal.model.CourseStudent;
import java.util.List;

@Service
public class CourseStudentService {
    @Autowired
    private CourseStudentRepository courseStudentRepository;

    public List<CourseStudent> getAllCourseStudents() {
        return courseStudentRepository.findAll();
    }

    public CourseStudent getCourseStudentById(Long id) {
        return courseStudentRepository.findById(id).orElse(null);
    }

    public CourseStudent createCourseStudent(CourseStudent courseStudent) {
        return courseStudentRepository.save(courseStudent);
    }

    public CourseStudent updateCourseStudent(Long id, CourseStudent courseStudent) {
        CourseStudent existingCourseStudent = courseStudentRepository.findById(id).orElse(null);
        if (existingCourseStudent == null) {
            throw new RuntimeException("Course student not found");
        }
        existingCourseStudent.setCourse(courseStudent.getCourse());
        existingCourseStudent.setUser(courseStudent.getUser());
        existingCourseStudent.setStatus(courseStudent.getStatus());
        existingCourseStudent.setGrade(courseStudent.getGrade());
        return courseStudentRepository.save(existingCourseStudent);
    }

    public void deleteCourseStudent(Long id) {
        courseStudentRepository.deleteById(id);
    } 
    
    public List<CourseStudent> getCourseStudentsByCourseId(Long courseId) {
        return courseStudentRepository.findByCourseId(courseId);
    }

    public List<CourseStudent> getCourseStudentsByUserId(Long userId) {
        return courseStudentRepository.findByUserId(userId);
    }

    public List<CourseStudent> getCourseStudentsByCourseIdAndUserId(Long courseId, Long userId) {
        return courseStudentRepository.findByCourseIdAndUserId(courseId, userId);
    }
    
}
