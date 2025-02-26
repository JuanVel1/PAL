package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "forums")
public class Forum {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;
}