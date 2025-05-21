package com.example.pal.dto;

import lombok.Data;
import java.util.List;

@Data
public class CourseUpdateDTO {
    private String title;
    private String description;
    private Long categoryId;
    private List<ContentDTO> contentsToAdd;
    private List<Long> contentIdsToRemove;
} 