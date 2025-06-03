package com.example.pal.service;

import com.example.pal.model.Certificate;
import com.example.pal.model.Course;
import com.example.pal.model.Exam;
import com.example.pal.model.User;
import com.example.pal.repository.CertificateRepository; 
import com.example.pal.repository.ExamRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import com.example.pal.model.Enrollment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository; 

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private PdfService pdfService;

    @Transactional
    public Certificate generateCertificate(Long courseId, Long userId) {

        Enrollment enrollment = enrollmentService.findByUserIdAndCourseId(userId, courseId).get(0);
        if (enrollment == null) {
            throw new RuntimeException("El usuario no está inscrito en el curso");
        }

        Course course = enrollment.getCourse();
        
        User user = enrollment.getUser();

        // Verificar si el estudiante ha aprobado todos los exámenes del curso
        List<Exam> courseExams = examRepository.findByCourseId(courseId);

        if (courseExams.isEmpty()) {
            throw new RuntimeException("El curso no tiene exámenes disponibles");
        }

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