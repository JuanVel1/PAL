package com.example.pal.dto;

import lombok.Data;
import java.util.List;

@Data
public class CourseProgressReportDTO {
    private Long courseId;
    private String courseTitle;
    private List<StudentProgressDTO> studentProgress;
    private CourseStatisticsDTO courseStatistics;

    @Data
    public static class StudentProgressDTO {
        private Long studentId;
        private String studentName;
        private double contentCompletionPercentage;
        private double averageExamScore;
        private int forumParticipationCount;
        private String status; // "Completado", "En progreso", "No iniciado"
    }

    @Data
    public static class CourseStatisticsDTO {
        private double averageContentCompletion;
        private double averageExamScore;
        private int totalStudents;
        private int completedStudents;
        private int inProgressStudents;
        private int notStartedStudents;
    }
} 