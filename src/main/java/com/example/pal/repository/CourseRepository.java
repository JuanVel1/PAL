package com.example.pal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.pal.model.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByTitle(String title);
    List<Course> findByCategoryName(String categoryName);

    
    /**
     * Busca cursos por término de búsqueda en título, descripción o categoría
     * y aplica filtros adicionales
     * 
     * @param searchTerm Término de búsqueda
     * @param isFree Filtro por precio (null para ignorar este filtro)
     * @param difficultyLevel Filtro por nivel de dificultad (null para ignorar este filtro)
     * @param pageable Objeto de paginación y ordenación
     * @return Página de cursos que cumplen los criterios
     */
    @Query("SELECT c FROM Course c WHERE " +
           "(:searchTerm IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           //"LOWER(c.category) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " + 
           "AND (:isFree IS NULL OR (:isFree = TRUE AND (c.price IS NULL OR c.price = 0)) OR (:isFree = FALSE AND c.price > 0)) " +
           "AND (:difficultyLevel IS NULL OR c.difficultyLevel = :difficultyLevel) ")
    Page<Course> findBySearchCriteria(
            @Param("searchTerm") String searchTerm,
            @Param("isFree") Boolean isFree,
            @Param("difficultyLevel") String difficultyLevel,
            Pageable pageable);

    /**
     * Aplica filtros sin término de búsqueda
     * 
     * @param isFree Filtro por precio (null para ignorar este filtro)
     * @param difficultyLevel Filtro por nivel de dificultad (null para ignorar este filtro)
     * @param pageable Objeto de paginación y ordenación
     * @return Página de cursos que cumplen los criterios
     */
    @Query("SELECT c FROM Course c WHERE " +
           "(:isFree IS NULL OR (:isFree = TRUE AND (c.price IS NULL OR c.price = 0)) OR (:isFree = FALSE AND c.price > 0)) " +
           "AND (:difficultyLevel IS NULL OR c.difficultyLevel = :difficultyLevel) ")
    Page<Course> findWithFilters(
            @Param("isFree") Boolean isFree,
            @Param("difficultyLevel") String difficultyLevel,
            Pageable pageable);
}