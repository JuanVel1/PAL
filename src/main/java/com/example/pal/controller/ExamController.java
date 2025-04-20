package com.example.pal.controller;

import com.example.pal.dto.*;
import com.example.pal.model.Exam;
import com.example.pal.model.ExamResult;
import com.example.pal.model.User;
import com.example.pal.repository.ExamRepository;
import com.example.pal.repository.ExamResultRepository;
import com.example.pal.repository.UserRepository;
import com.example.pal.service.ExamResultService;
import com.example.pal.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamResultService examResultService;

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<ExamResponseDTO>> getAllExams() {
        List<ExamResponseDTO> exams = examService.getAllExams();
        if (exams.isEmpty()) {
            ResponseDTO<ExamResponseDTO> response = new ResponseDTO<>("No exams found", null);
            return ResponseEntity.status(400).body(response);
        }
        ResponseDTO<ExamResponseDTO> response = new ResponseDTO<>("Exams retrieved successfully", exams);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/results/{examId}")
    public ResponseEntity<ResponseDTO<ExamResultDTO>> getExamResult(
            @PathVariable Long examId,
            @RequestParam Long studentId) {
        try {
            ExamResultDTO result = examResultService.getExamResult(examId, studentId);
            ResponseDTO<ExamResultDTO> response = new ResponseDTO<>("Exam results retrieved successfully", result);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseDTO<ExamResultDTO> response = new ResponseDTO<>(e.getMessage(), null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO<Exam>> deleteExam(@PathVariable Long id) {
        try {
            examService.deleteExamById(id);
            ResponseDTO<Exam> response = new ResponseDTO<>("Exam deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseDTO<Exam> response = new ResponseDTO<>(e.getMessage(), null);
            return ResponseEntity.status(400).body(response);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<ExamResponseDTO>> createExam(@RequestBody CreateExamDTO examDTO) {
        ExamResponseDTO createdExam = examService.createExam(examDTO);
        return ResponseEntity.ok(new ResponseDTO<>("Exam created successfully", createdExam));
    }

    @PostMapping("/submit/{examId}")
    public ResponseEntity<?> submitExam(
            @PathVariable Long examId,
            @RequestBody ExamSubmissionDTO submissionDTO) {

        // Buscar el examen
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        // Evaluar el examen
        int score = examService.evaluateExam(examId, submissionDTO.getAnswers());

        // Guardar el resultado del examen
        ExamResult examResult = new ExamResult();
        examResult.setExam(exam);
        User student = userRepository.findById(submissionDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        examResult.setStudent(student);
        examResult.setSubmissionDate(new Date());
        examResult.setAnswers(submissionDTO.getAnswers());
        examResult.setScore(score);
        examResultRepository.save(examResult);

        // Preparar la respuesta
        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("total", exam.getQuestions().size());

        return ResponseEntity.ok(result);
    }

}
