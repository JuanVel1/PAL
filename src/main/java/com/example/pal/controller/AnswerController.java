package com.example.pal.controller;

import com.example.pal.dto.AnswerDTO;
import com.example.pal.dto.ResponseDTO;
import com.example.pal.model.Answer;
import com.example.pal.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<Answer>> createAnswer(@RequestBody Answer answer) {
        Answer createdAnswer = answerService.createAnswer(answer);
        if (createdAnswer == null) {
            ResponseDTO<Answer> response = new ResponseDTO<>("Error creating answer", null);
            return ResponseEntity.status(400).body(response);
        }
        ResponseDTO<Answer> response = new ResponseDTO<>("Answer created successfully", createdAnswer);
        return ResponseEntity.status(201).body(response);
    }


    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<AnswerDTO>> getAllAnswers() {
        List<AnswerDTO> answers = answerService.getAllAnswers();
        if (answers.isEmpty()) {
            ResponseDTO<AnswerDTO> response = new ResponseDTO<>("No answers found", null);
            return ResponseEntity.status(400).body(response);
        }
        ResponseDTO<AnswerDTO> response = new ResponseDTO<>("Answers retrieved successfully", answers);
        return ResponseEntity.status(200).body(response);
    }

}
