package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "courses")
public class Course {
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private double price;
    
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private double average_grade;

}
