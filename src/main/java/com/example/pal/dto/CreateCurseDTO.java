package com.example.pal.dto;

import lombok.Data;

@Data
public class CreateCurseDTO {
    private String title;
    private String category;
    private Long instructorId;
    private String description;
    private double price;
    private String status;
    private double average_grade;
    
}
