package com.example.pal.service;

import com.example.pal.dto.AnswerDTO;
import com.example.pal.model.Answer;
import com.example.pal.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public  List<AnswerDTO> getAllAnswers() {
        List<Answer> answers = answerRepository.findAll();
        return answers.stream().map(answer -> {
            AnswerDTO dto = new AnswerDTO();
            dto.setId(answer.getId());
            dto.setText(answer.getText());
            dto.setCorrect(answer.isCorrect());
            dto.setQuestionId(answer.getQuestion().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }
}
