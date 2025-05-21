package com.example.pal.dto;

import lombok.Data;

@Data
public class CreateEnrollmentDTO {
    
    private Long userId;
    private Long courseId;
    private String enrollmentDate;
    private Long paymentId;
}
