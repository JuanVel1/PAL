package com.example.pal.service;

import com.example.pal.dto.CurseDTO;
import com.example.pal.dto.CourseSearchRequestDTO;
import com.example.pal.dto.CourseSearchResponseDTO;
import com.example.pal.model.Course;
import com.example.pal.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la búsqueda y filtrado de cursos
 */
@Service
public class CourseSearchService {

    @Autowired
    private CourseRepository courseRepository;

    /* 

    public CourseSearchService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
        */

    /**
     * Busca cursos según los criterios especificados y aplica filtros
     *
     * @param searchRequest DTO con criterios de búsqueda y filtros
     * @param page Número de página
     * @param size Tamaño de página
     * @return DTO con resultados de cursos y metadatos de paginación
     */
    public CourseSearchResponseDTO searchCourses(CourseSearchRequestDTO searchRequest, int page, int size) {
        // Configurar la paginación y ordenación
        Sort sort = createSort(searchRequest.getSortBy(), searchRequest.getSortDirection());
        Pageable pageable = PageRequest.of(page, size, sort);

        // Realizar la búsqueda con filtros
        Page<Course> coursesPage;
        
        if (searchRequest.getQuery() != null && !searchRequest.getQuery().trim().isEmpty()) {
            // Buscar por query en título, descripción o categoría
            coursesPage = courseRepository.findBySearchCriteria(
                    searchRequest.getQuery(),
                    getFilterPriceValue(searchRequest.getPriceFilter()),
                    getDifficultyLevelValue(searchRequest.getDifficultyLevel()),
                    pageable
            );
        } else {
            // Sin query, solo aplicar filtros
            coursesPage = courseRepository.findWithFilters(
                    getFilterPriceValue(searchRequest.getPriceFilter()),
                    getDifficultyLevelValue(searchRequest.getDifficultyLevel()),
                    pageable
            );
        }

        // Transformar resultados a DTOs
        List<CurseDTO> CurseDTOs = coursesPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        // Crear respuesta
        CourseSearchResponseDTO response = new CourseSearchResponseDTO();
        response.setCourses(CurseDTOs);
        response.setTotalResults((int) coursesPage.getTotalElements());
        
        response.setPage(page);
        response.setPageSize(size);
        response.setTotalPages(coursesPage.getTotalPages());
        
        return response;
    }

    /**
     * Crea un objeto Sort basado en los criterios de ordenación
     */
    private Sort createSort(CourseSearchRequestDTO.SortBy sortBy, CourseSearchRequestDTO.SortDirection sortDirection) {
        String sortProperty;
        
        switch (sortBy) {
            case PUBLICATION_DATE:
                sortProperty = "publicationDate";
                break;
            case RELEVANCE:
            default:
                // Para relevancia, si hay query usamos un enfoque especial en el repositorio
                // Si no hay query, ordenamos por rating y fecha
                if (sortDirection == CourseSearchRequestDTO.SortDirection.DESC) {
                    return Sort.by(
                        Sort.Order.desc("publicationDate")
                    );
                } else {
                    return Sort.by(
                        Sort.Order.asc("publicationDate")
                    );
                }
        }
        
        return sortDirection == CourseSearchRequestDTO.SortDirection.DESC ?
                Sort.by(Sort.Direction.DESC, sortProperty) :
                Sort.by(Sort.Direction.ASC, sortProperty);
    }
    
    /**
     * Convierte una entidad Course a CurseDTO
     */
    private CurseDTO mapToDTO(Course course) {
        CurseDTO dto = new CurseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setCategory(course.getCategory());
        dto.setPrice(course.getPrice());
        //dto.setIsFree(course.getPrice() == null || Double.valueOf(0).equals(course.getPrice()));
        dto.setDifficultyLevel(course.getDifficultyLevel());
        dto.setPublicationDate(course.getPublicationDate());
        dto.setDurationInHours(course.getDurationInHours());
        return dto;
    }
    
    /**
     * Obtiene el valor de filtro de precio para el repositorio
     */
    private Boolean getFilterPriceValue(CourseSearchRequestDTO.PriceFilter priceFilter) {
        if (priceFilter == null || priceFilter == CourseSearchRequestDTO.PriceFilter.ALL) {
            return null; // Null significa no aplicar este filtro
        }
        return priceFilter == CourseSearchRequestDTO.PriceFilter.FREE;
    }
    
    /**
     * Obtiene el valor de nivel de dificultad para el repositorio
     */
    private String getDifficultyLevelValue(CourseSearchRequestDTO.DifficultyLevel difficultyLevel) {
        if (difficultyLevel == null || difficultyLevel == CourseSearchRequestDTO.DifficultyLevel.ALL) {
            return null; // Null significa no aplicar este filtro
        }
        return difficultyLevel.name();
    }
}