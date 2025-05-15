package com.example.pal.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamResultDTO {
    private Long examId;
    private Long studentId;
    private String examTitle;
    private String studentName;
    private int score;
    private int totalQuestions;
    private double percentage;
    private List<QuestionResultDTO> questionResults;
    private String generalComments;

    public ExamResultDTO(Long examId, Long studentId, String title, String username, int correctAnswers,
                         int totalQuestions, double percentage, List<QuestionResultDTO> questionResults,
                         String generalComments) {
        this.examId = examId;
        this.studentId = studentId;
        this.examTitle = title;
        this.studentName = username;
        this.score = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.questionResults = questionResults;
        this.generalComments = generalComments;
    }
}
