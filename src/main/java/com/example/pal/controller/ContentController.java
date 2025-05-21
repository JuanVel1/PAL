package com.example.pal.controller;

import com.example.pal.dto.ContentDTO;
import com.example.pal.dto.ResponseDTO;
import com.example.pal.model.Content;
import com.example.pal.repository.ContentRepository;
import com.example.pal.service.ContentService;
import com.example.pal.service.FileStorageService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping("/api/content")
@CrossOrigin(origins = "http://localhost:5173")
public class ContentController {
    private final ContentService contentService;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<ContentDTO>> createContent(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @ModelAttribute ContentDTO contentDTO
    ) {
        try {
            if (file != null) {
                // Guardar el archivo
                String fileName = fileStorageService.storeFile(file);
                contentDTO.setNameFile(fileName);
            }

            return contentService.createContent(contentDTO);
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>("Error al subir el archivo: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<ContentDTO>> getAllContent() {
        return contentService.getAllContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ContentDTO>> getContentById(@PathVariable Long id) {
        return contentService.getContentById(id);
    }

    @GetMapping("/download/{id}")
    public void downloadContentFile(
            @PathVariable Long id,
            HttpServletResponse response) {
        try {
            Optional<Content> content = contentRepository.findById(id);

            if (content.isEmpty() || content.get().getNameFile() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado");
                return;
            }

            Path filePath = fileStorageService.getFilePath(content.get().getNameFile());

            if (!Files.exists(filePath)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado en disco");
                return;
            }

            byte[] fileBytes = Files.readAllBytes(filePath);
            String fileName = content.get().getNameFile();
            String contentType = Files.probeContentType(filePath);

            response.reset();
            response.setContentType(contentType != null ? contentType : "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentLength(fileBytes.length);
            response.getOutputStream().write(fileBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
        
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO<ContentDTO>> updateContent(@PathVariable Long id, @RequestBody ContentDTO contentDTO) {
        return contentService.updateContent(id, contentDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ResponseDTO<ContentDTO>> getByContentId(@PathVariable Long courseId) {
        return contentService.findByCourseId(courseId);
    }
}
