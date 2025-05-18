package com.example.pal.service;

import com.example.pal.dto.CreateExamDTO;
import com.example.pal.dto.ExamResponseDTO;
import com.example.pal.model.Answer;
import com.example.pal.model.Course;
import com.example.pal.model.Exam;
import com.example.pal.model.Question;
import com.example.pal.dto.QuestionDTO;
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

    public Exam createExam(CreateExamDTO examDTO) {
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

        return examRepository.save(exam);
    }

    public ExamResponseDTO mapToExamResponseDTO(Exam exam) {
        ExamResponseDTO dto = new ExamResponseDTO();
        dto.setId(exam.getId());
        dto.setTitle(exam.getTitle());
        dto.setCourseId(exam.getCourse().getId()); // Solo se asigna el ID

        List<QuestionDTO> questions = exam.getQuestions().stream().map(q -> {
            QuestionDTO qDto = new QuestionDTO();
            qDto.setId(q.getId());
            qDto.setText(q.getText());
            return qDto;
        }).collect(Collectors.toList());

        dto.setQuestions(questions);

        return dto;
    }


    public List<ExamResponseDTO> getAllExams() {
        List<Exam> exams = examRepository.findAll();

        return exams.stream().map(exam -> {
            List<QuestionDTO> questionDTOs = exam.getQuestions().stream().map(question -> {
                return new QuestionDTO(question.getId(), question.getText(), exam.getId());
            }).collect(Collectors.toList());
            return new ExamResponseDTO(exam.getId(), exam.getTitle(), exam.getCourse().getId(), questionDTOs);
        }).collect(Collectors.toList());
    }

    public List<ExamResponseDTO> getAllExamsByCourse(Long courseId) {
        List<Exam> exams = examRepository.findByCourseId(courseId);

        return exams.stream().map(exam -> {
            List<QuestionDTO> questionDTOs = exam.getQuestions().stream().map(question -> {
                return new QuestionDTO(question.getId(), question.getText(), exam.getId());
            }).collect(Collectors.toList());
            return new ExamResponseDTO(exam.getId(), exam.getTitle(), exam.getCourse().getId(), questionDTOs);
        }).collect(Collectors.toList());
    }

    public void deleteExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + id));
        examRepository.delete(exam);
    }

    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
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
