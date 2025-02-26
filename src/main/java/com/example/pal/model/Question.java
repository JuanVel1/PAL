package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(nullable = false)
    private String text;

    // Otros atributos de la pregunta, como opciones, respuesta correcta, etc.
}