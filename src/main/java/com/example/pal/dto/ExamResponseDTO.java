package com.example.pal.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamResponseDTO {
    private Long id;
    private String title;
    private Long courseId; // Cambiado a solo el ID
    private List<QuestionDTO> questions;

    public ExamResponseDTO(Long id, String title, Long courseId, List<QuestionDTO> questions) {
        this.id = id;
        this.title = title;
        this.courseId = courseId;
        this.questions = questions;
    }

    public ExamResponseDTO() {
    }
}