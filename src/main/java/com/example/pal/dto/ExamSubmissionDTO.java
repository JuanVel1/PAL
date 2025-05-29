package com.example.pal.dto;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionDTO {
    private Long studentId;
    private Map<Long, String> answers;
}