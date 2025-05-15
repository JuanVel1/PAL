package com.example.pal.service;

import com.example.pal.dto.ExamResultDTO;
import com.example.pal.dto.QuestionResultDTO;
import com.example.pal.model.*;
import com.example.pal.repository.ExamRepository;
import com.example.pal.repository.ExamResultRepository;
import com.example.pal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExamResultService {
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamResultRepository examResultRepository;

    public ExamResultDTO getExamResult(Long examId, Long studentId) {
        // Buscar el examen
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + examId));

        // Buscar el estudiante
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // Buscar los resultados del examen para este estudiante
        ExamResult examResult = examResultRepository.findByExamAndStudent(exam, student)
                .orElseThrow(() -> new RuntimeException("Exam result not found for student ID: " + studentId + " and exam ID: " + examId));

        // Obtener las respuestas del estudiante
        Map<Long, String> studentAnswers = examResult.getAnswers();

        // Crear la lista de resultados por pregunta
        List<QuestionResultDTO> questionResults = new ArrayList<>();

        for (Question question : exam.getQuestions()) {
            String studentAnswer = studentAnswers.getOrDefault(question.getId(), "No respondida");

            // Encontrar la respuesta correcta para esta pregunta
            String correctAnswer = question.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .map(Answer::getText)
                    .findFirst()
                    .orElse("No definida");

            // Determinar si la respuesta del estudiante es correcta
            boolean isCorrect = studentAnswer.equalsIgnoreCase(correctAnswer);

            questionResults.add(new QuestionResultDTO(
                    question.getId(),
                    question.getText(),
                    studentAnswer,
                    correctAnswer,
                    isCorrect
            ));
        }

        // Calcular estadísticas
        int totalQuestions = exam.getQuestions().size();
        int correctAnswers = (int) questionResults.stream().filter(QuestionResultDTO::isCorrect).count();
        double percentage = totalQuestions > 0 ? (correctAnswers * 100.0 / totalQuestions) : 0;


        // Generar comentarios generales basados en el porcentaje
        String generalComments = generateComments(percentage);

        // Crear y devolver el DTO con los resultados
        return new ExamResultDTO(
                examId,
                studentId,
                exam.getTitle(),
                student.getUsername(),
                correctAnswers,
                totalQuestions,
                percentage,
                questionResults,
                generalComments
        );
    }

    private String generateComments(double percentage) {
        if (percentage >= 90) {
            return "¡Excelente trabajo! Has demostrado un conocimiento sobresaliente.";
        } else if (percentage >= 80) {
            return "Muy buen trabajo. Has demostrado un buen dominio del tema.";
        } else if (percentage >= 70) {
            return "Buen trabajo. Has demostrado un conocimiento adecuado.";
        } else if (percentage >= 60) {
            return "Aprobado. Has demostrado un conocimiento básico del tema.";
        } else {
            return "Necesitas repasar más el tema. Te recomendamos revisar el material nuevamente.";
        }
    }
}
