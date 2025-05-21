package com.example.pal.dto;

import java.util.Date;

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
    private String difficultyLevel;
    private Date publicationDate;
    private int durationInHours;

}
