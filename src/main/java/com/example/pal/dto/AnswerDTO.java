package com.example.pal.dto;

import lombok.Data;

@Data
public class AnswerDTO {
    private Long Id;
    private Long questionId;
    private String text;
    private boolean correct;
}


