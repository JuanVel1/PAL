package com.example.pal.dto;
import lombok.Data;
import com.example.pal.model.Exam;
import lombok.Getter;

@Data
public class CreateQuestionDTO {
    @Getter
    private String text;
    private Exam exam;

}
