package com.example.pal.service;

import com.example.pal.model.Certificate;
import com.example.pal.model.Course;
import com.example.pal.model.Exam;
import com.example.pal.model.User;
import com.example.pal.repository.CertificateRepository;
import com.example.pal.repository.CourseRepository;
import com.example.pal.repository.ExamRepository;
import com.example.pal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private PdfService pdfService;

    @Transactional
    public Certificate generateCertificate(Long courseId, Long userId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el estudiante ha aprobado todos los exámenes del curso
        List<Exam> courseExams = examRepository.findByCourseId(courseId);
        boolean allExamsPassed = courseExams.stream()
                .allMatch(exam -> exam.getExamResults().stream()
                        .anyMatch(result -> result.getStudent().getId().equals(userId) && result.getScore() >= 3));

        if (!allExamsPassed) {
            throw new RuntimeException("El estudiante no ha aprobado todos los exámenes del curso");
        }

        // Crear el certificado
        Certificate certificate = new Certificate();
        certificate.setUser(user);
        certificate.setCourse(course);
        certificate.setIssueDate(LocalDate.now());
        certificate.setCertificateNumber(UUID.randomUUID().toString());
        certificate.setStatus("GENERATED");

        return certificateRepository.save(certificate);
    }

    public byte[] downloadCertificate(Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado"));

        // Actualizar el estado del certificado
        certificate.setStatus("DOWNLOADED");
        certificateRepository.save(certificate);

        // Generar el PDF del certificado
        return pdfService.generateCertificatePdf(certificate);
    }

    public List<Certificate> getUserCertificates(Long userId) {
        return certificateRepository.findByUserId(userId);
    }
} 