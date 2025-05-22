package com.example.pal.controller;

import com.example.pal.dto.ResponseDTO;
import com.example.pal.model.Certificate;
import com.example.pal.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/generate/{courseId}/{userId}")
    public ResponseEntity<ResponseDTO<Certificate>> generateCertificate(
            @PathVariable Long courseId,
            @PathVariable Long userId) {
        try {
            Certificate certificate = certificateService.generateCertificate(courseId, userId);
            ResponseDTO<Certificate> response = new ResponseDTO<>("Certificate generated succesfully", certificate);
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            ResponseDTO<Certificate> response = new ResponseDTO<>(e.getMessage(), null);
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/download/{certificateId}")
    public void downloadCertificate(
            @PathVariable Long certificateId,
            HttpServletResponse response) {
        try {
            byte[] pdfBytes = certificateService.downloadCertificate(certificateId);
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=certificate.pdf");
            response.getOutputStream().write(pdfBytes);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Certificate>> getUserCertificates(@PathVariable Long userId) {
        List<Certificate> certificates = certificateService.getUserCertificates(userId);
        return ResponseEntity.ok(certificates);
    }
} 