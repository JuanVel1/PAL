package com.example.pal.service;
import org.springframework.stereotype.Service; 
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.ByteArrayOutputStream; 
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Map;
import com.example.pal.model.Certificate;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.borders.Border;

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

    public byte[] generateCertificatePdf(Certificate certificate) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());

            // Configurar márgenes para centrado vertical
            document.setMargins(50, 50, 50, 50);

            // Crear una tabla para centrado vertical
            Table table = new Table(1);
            table.setWidth(UnitValue.createPercentValue(100));
            table.setHeight(UnitValue.createPercentValue(100));

            // Celda para el contenido
            Cell cell = new Cell();
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
            cell.setBorder(Border.NO_BORDER);

            // Contenedor para el contenido
            Div content = new Div();
            content.setWidth(UnitValue.createPercentValue(100));
            content.setTextAlignment(TextAlignment.CENTER);

            // Agregar el logo de la plataforma
            Image logo = new Image(ImageDataFactory.create("src/main/resources/images/logo.png"));
            logo.setWidth(100);
            content.add(logo);

            // Agregar espacio después del logo
            content.add(new Paragraph("\n"));

            // Agregar el título del certificado
            content.add(new Paragraph("CERTIFICADO DE COMPLETACIÓN")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            // Agregar espacio después del título
            content.add(new Paragraph("\n"));

            // Agregar el contenido del certificado
            content.add(new Paragraph("Este certificado se otorga a:")
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));
            
            content.add(new Paragraph(certificate.getUser().getUsername())
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            content.add(new Paragraph("\n"));

            content.add(new Paragraph("Por haber completado exitosamente el curso:")
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));
            
            content.add(new Paragraph(certificate.getCourse().getTitle())
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            content.add(new Paragraph("\n"));

            // Agregar la fecha de emisión
            content.add(new Paragraph("Fecha de emisión: " + certificate.getIssueDate())
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));

            // Agregar el número de certificado
            content.add(new Paragraph("Número de certificado: " + certificate.getCertificateNumber())
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));

            // Agregar el contenido a la celda
            cell.add(content);
            table.addCell(cell);

            // Agregar la tabla al documento
            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el certificado PDF", e);
        }
    }
}