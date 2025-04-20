package com.example.pal.dto;

import lombok.Data;

@Data
public class QuestionResultDTO {
    private Long questionId;
    private String questionText;
    private String studentAnswer;
    private String correctAnswer;
    private boolean isCorrect;


    public QuestionResultDTO(Long id, String text, String studentAnswer, String correctAnswer, boolean isCorrect) {
        this.questionId = id;
        this.questionText = text;
        this.studentAnswer = studentAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }
}
