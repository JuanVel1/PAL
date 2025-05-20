package com.example.pal.controller;

import com.example.pal.dto.CourseSearchRequestDTO;
import com.example.pal.dto.CourseSearchResponseDTO;
import com.example.pal.service.CourseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la búsqueda y filtrado de cursos
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseSearchController {

    @Autowired
    private CourseSearchService courseSearchService;

    public CourseSearchController(CourseSearchService courseSearchService) {
        this.courseSearchService = courseSearchService;
    }

    /**
     * Endpoint para buscar cursos con filtros
     * @param searchRequest DTO con los parámetros de búsqueda
     * @return Respuesta con resultados de cursos filtrados
     */
    @GetMapping("/search")
    public ResponseEntity<CourseSearchResponseDTO> searchCourses(
            @RequestBody CourseSearchRequestDTO searchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {

        CourseSearchResponseDTO response = courseSearchService.searchCourses(searchRequest, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint alternativo que recibe los parámetros de búsqueda como un objeto JSON en el cuerpo de la petición
     * 
     * @param searchRequest DTO con los parámetros de búsqueda
     * @param page Número de página
     * @param size Tamaño de página
     * @return Respuesta con resultados de cursos filtrados
     */
    @PostMapping("/search")
    public ResponseEntity<CourseSearchResponseDTO> searchCoursesPost(
            @RequestBody CourseSearchRequestDTO searchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        CourseSearchResponseDTO response = courseSearchService.searchCourses(searchRequest, page, size);
        return ResponseEntity.ok(response);
    }
}