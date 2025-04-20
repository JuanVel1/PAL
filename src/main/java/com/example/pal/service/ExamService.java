package com.example.pal.service;

import com.example.pal.dto.CreateExamDTO;
import com.example.pal.dto.ExamResponseDTO;
import com.example.pal.model.Answer;
import com.example.pal.model.Course;
import com.example.pal.model.Exam;
import com.example.pal.model.Question;
import com.example.pal.repository.AnswerRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public ExamResponseDTO createExam(CreateExamDTO examDTO) {
        if (courseRepository.findByTitle(examDTO.getTitle()) != null) {
            throw new RuntimeException("Exam already exists");
        }

        Course course = courseRepository.findById(examDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + examDTO.getCourseId()));

        Exam exam = new Exam();
        exam.setTitle(examDTO.getTitle());
        exam.setCourse(course);

        List<Question> questions = examDTO.getQuestions().stream()
                .map(questionDTO -> {
                    Question question = new Question();
                    question.setText(questionDTO.getText());
                    question.setExam(exam);
                    return question;
                })
                .collect(Collectors.toList());

        exam.setQuestions(questions);

        Exam savedExam = examRepository.save(exam);
        return new ExamResponseDTO(savedExam);
    }


    public List<ExamResponseDTO> getAllExams() {
        return examRepository.findAll().stream()
                .map(ExamResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ExamResponseDTO getExamResults(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + examId));
        return new ExamResponseDTO(exam);
    }

    public void deleteExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + id));
        examRepository.delete(exam);
    }

    public int evaluateExam(Long examId, Map<Long, String> answersUser) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Examen no encontrado"));

        int score = 0;

        for (Question question : exam.getQuestions()) {
            Long userResponseId = Long.valueOf(answersUser.get(question.getId()));

            Optional<Answer> answer = answerRepository.findById(userResponseId);

            if (answer.isPresent() && answer.get().isCorrect()) {
                score++;
            }
        }

        return score;
    }

}
