package com.example.pal.dto;

import lombok.Data;

@Data
public class ContentDTO {
    private Long id;
    private Long courseId;
    private String type;
    private String fileUrl;
}
