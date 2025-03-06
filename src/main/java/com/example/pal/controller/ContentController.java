// ContentController.java
package com.example.pal.controller;

import com.example.pal.dto.ContentDTO;
import com.example.pal.dto.ContentUploadDTO;
import com.example.pal.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @PostMapping("/upload")
    public ResponseEntity<ContentDTO> uploadContent(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("type") String type,
                                                    @RequestParam("courseId") Long courseId) throws IOException {
        // Crea un DTO con los parámetros de la solicitud
        ContentUploadDTO contentUploadDTO = new ContentUploadDTO();
        contentUploadDTO.setFile(file);
        contentUploadDTO.setType(type);
        contentUploadDTO.setCourseId(courseId);

        // Llama al servicio para manejar la carga
        ContentDTO contentDTO = contentService.uploadContent(contentUploadDTO);

        // Devuelve la respuesta con el DTO
        return ResponseEntity.ok(contentDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContentDTO>> getAllContent() {
        List<ContentDTO> contentList = contentService.getAllContent();
        return ResponseEntity.ok(contentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContentById(@PathVariable Long id) {
        ContentDTO contentDTO = contentService.getContentById(id);
        return contentDTO != null ? ResponseEntity.ok(contentDTO) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateContent(@PathVariable Long id, @ModelAttribute ContentUploadDTO contentUploadDTO) throws IOException {
        contentService.updateContent(id, contentUploadDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}
