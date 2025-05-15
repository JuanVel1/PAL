package com.example.pal.dto;

import lombok.Data;

@Data
public class QuestionResponseDTO {
    private String text;
    private Long id;
    private Long examId;

    public QuestionResponseDTO(Long id, String text, Long examId) {
        this.id = id;
        this.text = text;
        this.examId = examId;
    }
}
