package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "answer")
public class Answer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean correct;
}
