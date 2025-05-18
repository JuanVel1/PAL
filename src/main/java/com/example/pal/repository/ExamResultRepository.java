package com.example.pal.repository;

import com.example.pal.model.Exam;
import com.example.pal.model.ExamResult;
import com.example.pal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    Optional<ExamResult> findByExamAndStudent(Exam exam, User student);
    List<ExamResult> findByStudentIdAndExam_CourseId(Long studentId, Long courseId);
}
