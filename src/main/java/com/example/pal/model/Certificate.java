package com.example.pal.model;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;
 

@Data
@Entity
@Table(name = "certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private String certificateNumber;

    @Column(nullable = false)
    private String status; // PENDING, GENERATED, DOWNLOADED
}
