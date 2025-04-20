package com.example.pal.controller;

import com.example.pal.dto.QuestionDTO;
import com.example.pal.dto.QuestionResponseDTO;
import com.example.pal.dto.ResponseDTO;
import com.example.pal.model.Question;
import com.example.pal.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<QuestionResponseDTO>> getAllQuestions() {
        List<QuestionResponseDTO> questions = questionService.getAllQuestions();
        if (questions.isEmpty()) {
            ResponseDTO<QuestionResponseDTO> response = new ResponseDTO<>("No questions found", null);
            return ResponseEntity.status(400).body(response);
        }
        ResponseDTO<QuestionResponseDTO> response = new ResponseDTO<>("Questions retrieved successfully", questions);
        return ResponseEntity.status(200).body(response);
    }
}
