package com.example.pal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pal.model.CourseStudent;

public interface CourseStudentRepository extends JpaRepository<CourseStudent, Long> {
    List<CourseStudent> findByUserId(Long userId);
    List<CourseStudent> findByCourseIdAndUserId(Long courseId, Long userId);
    List<CourseStudent> findByCourseId(Long courseId);
}
