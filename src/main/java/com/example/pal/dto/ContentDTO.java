package com.example.pal.dto;

import lombok.Data;

@Data
public class ContentDTO {
    private Long id;
    private String fileUrl;
    private String type;
    private Long courseId;
    private String nameFile;
    private byte[] fileBytes;
}
