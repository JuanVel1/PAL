package com.example.pal.dto;

import com.example.pal.model.Exam;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ExamResponseDTO {
    private Long id;
    private String title;
    private Long courseId;
    private List<QuestionDTO> questions;

    public ExamResponseDTO(Exam exam) {
        this.id = exam.getId();
        this.title = exam.getTitle();
        this.courseId = exam.getCourse().getId();
        this.questions = exam.getQuestions().stream()
                .map(q -> new QuestionDTO(q.getId(), q.getText(), q.getExam().getId()))
                .collect(Collectors.toList());
    }
}
