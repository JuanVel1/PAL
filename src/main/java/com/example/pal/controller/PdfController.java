package com.example.pal.controller;

import com.example.pal.dto.PdfRequestDto;
import com.example.pal.service.PdfService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
 


@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/generate-pdf")
    public void generatePdf(@RequestBody PdfRequestDto requestDto, HttpServletResponse response) throws IOException {

        // Define the paths to the HTML template and output PDF        
        String templatePath = "src/main/resources/templates/pdfEmail.html";

         // Convert DTO to a Map for placeholder replacement
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("nombre", requestDto.getName());
        placeholders.put("monto", requestDto.getAmount());

        byte[] pdfContents = pdfService.generatePdf(templatePath, placeholders);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=factura.pdf");
        response.getOutputStream().write(pdfContents);
    }
}  