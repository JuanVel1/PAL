package com.example.pal.dto;

import java.util.Map;

import lombok.Data;

@Data
public class ExamSubmissionDTO {
    private Long studentId;
    private Map<Long, String> answers;


    public ExamSubmissionDTO(Long studentId, Map<Long, String> answers) {
        this.studentId = studentId;
        this.answers = answers;
    }
 
}