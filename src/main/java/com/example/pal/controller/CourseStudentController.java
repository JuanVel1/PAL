package com.example.pal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pal.dto.ResponseDTO;
import com.example.pal.model.CourseStudent;
import com.example.pal.service.CourseStudentService;

@RestController
@RequestMapping("/api/course-student")
public class CourseStudentController {
    
    @Autowired
    private CourseStudentService courseStudentService;

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<List<CourseStudent>>> getAllCourseStudents() {
        List<CourseStudent> courseStudents = courseStudentService.getAllCourseStudents();
        ResponseDTO<List<CourseStudent>> response = new ResponseDTO<List<CourseStudent>>("Course students retrieved successfully", courseStudents);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CourseStudent>> getCourseStudentById(@PathVariable Long id) {
        CourseStudent courseStudent = courseStudentService.getCourseStudentById(id);
        ResponseDTO<CourseStudent> response = new ResponseDTO<CourseStudent>("Course student retrieved successfully", courseStudent);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<CourseStudent>> createCourseStudent(@RequestBody CourseStudent courseStudent) {
        CourseStudent createdCourseStudent = courseStudentService.createCourseStudent(courseStudent);
        ResponseDTO<CourseStudent> response = new ResponseDTO<CourseStudent>("Course student created successfully", createdCourseStudent);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO<CourseStudent>> updateCourseStudent(@PathVariable Long id, @RequestBody CourseStudent courseStudent) {
        CourseStudent updatedCourseStudent = courseStudentService.updateCourseStudent(id, courseStudent);
        ResponseDTO<CourseStudent> response = new ResponseDTO<CourseStudent>("Course student updated successfully", updatedCourseStudent);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteCourseStudent(@PathVariable Long id) {
        courseStudentService.deleteCourseStudent(id);
        ResponseDTO<Void> response = new ResponseDTO<>("Course student deleted successfully",null);
        return ResponseEntity.ok(response);
    }
}
