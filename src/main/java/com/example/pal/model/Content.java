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

    @Column(name = "type")
    private String type;

    @Column(name = "file_url")
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
