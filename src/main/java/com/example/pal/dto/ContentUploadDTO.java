package com.example.pal.dto;

import lombok.Data;

@Data
public class ContentUploadDTO {
    private String title;
    private String description;
    private Long courseId;
    private Integer order;
}
