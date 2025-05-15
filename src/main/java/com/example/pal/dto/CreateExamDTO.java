package com.example.pal.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateExamDTO {
    @NotNull
    private Long  courseId;
    @NotNull
    private String title;
    @NotNull
    private List<CreateQuestionDTO> questions;
}
