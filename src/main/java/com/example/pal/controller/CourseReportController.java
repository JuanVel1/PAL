package com.example.pal.controller;

import com.example.pal.dto.CourseProgressReportDTO;
import com.example.pal.service.CourseReportService;
import com.example.pal.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class CourseReportController {

    @Autowired
    private CourseReportService courseReportService;

    @Autowired
    private PdfService pdfService;

    @GetMapping("/progress/{courseId}")
    public ResponseEntity<?> getCourseProgressReport(
            @PathVariable Long courseId,
            @RequestParam(required = false, defaultValue = "json") String format) {
        
        CourseProgressReportDTO report = courseReportService.generateProgressReport(courseId);

        if ("csv".equalsIgnoreCase(format)) {
            return generateCsvResponse(report);
        } else if ("pdf".equalsIgnoreCase(format)) {
            return generatePdfResponse(report);
        }

        return ResponseEntity.ok(report);
    }

    private ResponseEntity<byte[]> generateCsvResponse(CourseProgressReportDTO report) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);

            // Escribir encabezados
            writer.write("ID Estudiante,Nombre,Porcentaje Completado,Promedio Exámenes,Participación Foros,Estado\n");

            // Escribir datos de estudiantes
            for (CourseProgressReportDTO.StudentProgressDTO student : report.getStudentProgress()) {
                writer.write(String.format("%d,%s,%.2f,%.2f,%d,%s\n",
                        student.getStudentId(),
                        student.getStudentName(),
                        student.getContentCompletionPercentage(),
                        student.getAverageExamScore(),
                        student.getForumParticipationCount(),
                        student.getStatus()));
            }

            writer.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "course_progress_report.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<byte[]> generatePdfResponse(CourseProgressReportDTO report) {
        try {
            // Crear un mapa con los datos para el template
            Map<String, String> data = Map.of(
                "courseTitle", report.getCourseTitle(),
                "totalStudents", String.valueOf(report.getCourseStatistics().getTotalStudents()),
                "completedStudents", String.valueOf(report.getCourseStatistics().getCompletedStudents()),
                "inProgressStudents", String.valueOf(report.getCourseStatistics().getInProgressStudents()),
                "notStartedStudents", String.valueOf(report.getCourseStatistics().getNotStartedStudents()),
                "averageCompletion", String.format("%.2f", report.getCourseStatistics().getAverageContentCompletion()),
                "averageExamScore", String.format("%.2f", report.getCourseStatistics().getAverageExamScore())
            );

            byte[] pdfBytes = pdfService.generatePdf("src/main/resources/templates/course_report.html", data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "course_progress_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 