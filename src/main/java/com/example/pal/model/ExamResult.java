package com.example.pal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "exam_results")
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(nullable = false)
    private Date submissionDate;

    @ElementCollection
    @CollectionTable(name = "student_answers",
            joinColumns = @JoinColumn(name = "result_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "answer_text")
    private Map<Long, String> answers;

    @Column
    private Integer score;
}
