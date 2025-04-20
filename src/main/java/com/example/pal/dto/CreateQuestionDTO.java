package com.example.pal.dto;

import com.example.pal.model.Exam;
import lombok.Getter;

public class CreateQuestionDTO {
    @Getter
    private String text;
    private Exam exam;

}
