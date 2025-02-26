package com.example.pal.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "content")
public class Content {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    private String type;
}
