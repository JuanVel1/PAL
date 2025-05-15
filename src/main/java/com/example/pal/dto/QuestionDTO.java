package com.example.pal.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionDTO {
    private Long id;
    private String text;
    private Long examId;

    public QuestionDTO(Long id, String text, Long examId) {
        this.id = id;
        this.text = text;
        this.examId = examId;
    }
}
