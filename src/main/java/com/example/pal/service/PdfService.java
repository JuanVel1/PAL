package com.example.pal.service;
import org.springframework.stereotype.Service; 
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream; 
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Map;

@Service
public class PdfService {

    public byte[] generatePdf(String templatePath, Map<String, String> placeholders) throws IOException {
        String htmlContent = new String(Files.readAllBytes(Paths.get(templatePath)), StandardCharsets.UTF_8);

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            htmlContent = htmlContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
    
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            HtmlConverter.convertToPdf(htmlContent, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}