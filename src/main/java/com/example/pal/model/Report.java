package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
public class Report {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String data;
}