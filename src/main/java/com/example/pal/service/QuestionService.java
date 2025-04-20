package com.example.pal.service;

import com.example.pal.dto.QuestionDTO;
import com.example.pal.dto.QuestionResponseDTO;
import com.example.pal.model.Question;
import com.example.pal.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionResponseDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(question -> new QuestionResponseDTO(question.getId(), question.getText(), question.getExam().getId()))
                .toList();
    }
}
