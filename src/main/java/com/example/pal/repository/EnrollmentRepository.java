package com.example.pal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pal.model.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // Métodos personalizados para la lógica de negocio relacionada con las inscripciones
    // Por ejemplo, encontrar inscripciones por usuario o curso
    List<Enrollment> findByUserId(Long userId);
    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);

    
}
