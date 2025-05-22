package com.example.pal.dto;

import com.example.pal.model.Category;

import lombok.Data;
import java.util.Date;

@Data
/**
 * DTO para representar un curso en los resultados de búsqueda
 */
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Category category;
    private Double price;
    private Boolean isFree;
    private String difficultyLevel;
    private String instructorName;
    private Double averageRating;
    private Double averageGrade;
    private Date publicationDate;
    private Integer durationInHours;

}