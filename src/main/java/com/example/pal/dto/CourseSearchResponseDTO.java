package com.example.pal.dto;

import java.util.List;

import lombok.Data;

/**
 * DTO para la respuesta de búsqueda de cursos
 * Contiene los resultados de la búsqueda y metadatos (total, paginación, etc.)
 */
@Data
public class CourseSearchResponseDTO {
    private List<CurseDTO> courses;
    private int totalResults;
    private int page;
    private int pageSize;
    private int totalPages;

}