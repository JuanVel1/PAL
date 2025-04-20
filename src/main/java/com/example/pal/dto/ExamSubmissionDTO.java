package com.example.pal.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ExamSubmissionDTO {
    private Long studentId;
    private Map<Long, String> answers;
}