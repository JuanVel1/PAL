package com.example.pal.dto;

import lombok.Data;

/**
 * DTO para solicitudes de búsqueda de cursos
 * Contiene todos los parámetros de búsqueda y filtros posibles
 */

 @Data
public class CourseSearchRequestDTO {
    private String query; // Término de búsqueda (título, descripción o categoría)
    private PriceFilter priceFilter; // Filtro de precio (GRATIS, PAGO, TODOS)
    private DifficultyLevel difficultyLevel; // Nivel de dificultad
    private SortBy sortBy; // Criterio de ordenación
    private SortDirection sortDirection; // Dirección de ordenación

    // Enum para el filtro de precio
    public enum PriceFilter {
        FREE, PAID, ALL
    }

    // Enum para el nivel de dificultad
    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, ALL;
        //Obtener texto del enum

        public String getText() {
            switch (this) {
                case BEGINNER:
                    return "BEGINNER";
                case INTERMEDIATE:
                    return "INTERMEDIATE";
                case ADVANCED:
                    return "ADVANCED";
                default:
                    return "ALL";
            }
        }
    }

    // Enum para ordenación
    public enum SortBy {
        RELEVANCE, PUBLICATION_DATE, RATING
    }

    // Enum para dirección de ordenación
    public enum SortDirection {
        ASC, DESC
    }
}