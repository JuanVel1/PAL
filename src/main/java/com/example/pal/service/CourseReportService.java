package com.example.pal.service;

import com.example.pal.dto.CourseProgressReportDTO;
import com.example.pal.model.Course;
import com.example.pal.model.Enrollment;
import com.example.pal.model.User;
import com.example.pal.model.ExamResult;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.ExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseReportService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ExamResultRepository examResultRepository;

    public CourseProgressReportDTO generateProgressReport(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        CourseProgressReportDTO report = new CourseProgressReportDTO();
        report.setCourseId(courseId);
        report.setCourseTitle(course.getTitle());

        // Obtener todos los estudiantes inscritos en el curso
        List<User> students = enrollmentService.findByCourseId(courseId).stream().map(Enrollment::getUser)
                .collect(Collectors.toList());

        // Generar el progreso de cada estudiante
        List<CourseProgressReportDTO.StudentProgressDTO> studentProgress = students.stream()
                .map(student -> generateStudentProgress(student, courseId))
                .collect(Collectors.toList());

        report.setStudentProgress(studentProgress);

        // Calcular estadísticas del curso
        report.setCourseStatistics(calculateCourseStatistics(studentProgress));

        return report;
    }

    private CourseProgressReportDTO.StudentProgressDTO generateStudentProgress(User student, Long courseId) {
        CourseProgressReportDTO.StudentProgressDTO progress = new CourseProgressReportDTO.StudentProgressDTO();
        progress.setStudentId(student.getId());
        progress.setStudentName(student.getUsername());

        // Calcular porcentaje de contenido completado
        double contentCompletion = calculateContentCompletion(student.getId(), courseId);
        progress.setContentCompletionPercentage(contentCompletion);

        // Calcular promedio de calificaciones en exámenes
        double averageExamScore = calculateAverageExamScore(student.getId(), courseId);
        progress.setAverageExamScore(averageExamScore);

        // Calcular participación en foros
        int forumParticipation = calculateForumParticipation(student.getId(), courseId);
        progress.setForumParticipationCount(forumParticipation);

        // Determinar estado del estudiante
        progress.setStatus(determineStudentStatus(contentCompletion, averageExamScore));

        return progress;
    }

    private double calculateContentCompletion(Long studentId, Long courseId) {
        return 75.0;
    }

    private double calculateAverageExamScore(Long studentId, Long courseId) {
        List<ExamResult> examResults = examResultRepository.findByStudentIdAndExam_CourseId(studentId, courseId);
        if (examResults.isEmpty()) {
            return 0.0;
        }
        return examResults.stream()
                .mapToDouble(ExamResult::getScore)
                .average()
                .orElse(0.0);
    }

    private int calculateForumParticipation(Long studentId, Long courseId) {
        // TODO: Implementar lógica para calcular la participación en foros
        // Por ahora retornamos un valor de ejemplo
        return 5;
    }

    private String determineStudentStatus(double contentCompletion, double averageExamScore) {
        if (contentCompletion >= 90 && averageExamScore >= 80) {
            return "Completado";
        } else if (contentCompletion > 0) {
            return "En progreso";
        } else {
            return "No iniciado";
        }
    }

    private CourseProgressReportDTO.CourseStatisticsDTO calculateCourseStatistics(
            List<CourseProgressReportDTO.StudentProgressDTO> studentProgress) {
        CourseProgressReportDTO.CourseStatisticsDTO statistics = new CourseProgressReportDTO.CourseStatisticsDTO();

        statistics.setTotalStudents(studentProgress.size());
        statistics.setCompletedStudents((int) studentProgress.stream()
                .filter(p -> "Completado".equals(p.getStatus()))
                .count());
        statistics.setInProgressStudents((int) studentProgress.stream()
                .filter(p -> "En progreso".equals(p.getStatus()))
                .count());
        statistics.setNotStartedStudents((int) studentProgress.stream()
                .filter(p -> "No iniciado".equals(p.getStatus()))
                .count());

        statistics.setAverageContentCompletion(studentProgress.stream()
                .mapToDouble(CourseProgressReportDTO.StudentProgressDTO::getContentCompletionPercentage)
                .average()
                .orElse(0.0));

        statistics.setAverageExamScore(studentProgress.stream()
                .mapToDouble(CourseProgressReportDTO.StudentProgressDTO::getAverageExamScore)
                .average()
                .orElse(0.0));

        return statistics;
    }
}