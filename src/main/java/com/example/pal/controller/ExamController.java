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

import java.util.*;

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

    @GetMapping("/{examId}")
    public ResponseEntity<ResponseDTO<ExamResponseDTO>> getExamById(@PathVariable Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        ExamResponseDTO examDTO = examService.mapToExamResponseDTO(exam);

        return ResponseEntity.ok(new ResponseDTO<>("Exam retrieved successfully", examDTO));
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
    public ResponseEntity<ResponseDTO<Exam>> createExam(@RequestBody CreateExamDTO examDTO) {
        Exam createdExam = examService.createExam(examDTO);
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

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ResponseDTO<List<ExamResponseDTO>>> getCourseResults(
            @PathVariable Long courseId) {
        List<ExamResponseDTO> results = examService.getAllExamsByCourse(courseId);
        ResponseDTO<List<ExamResponseDTO>> response = new ResponseDTO<List<ExamResponseDTO>>("Exams retrieved successfully", results);
        return ResponseEntity.status(200).body(response);
    }
}



