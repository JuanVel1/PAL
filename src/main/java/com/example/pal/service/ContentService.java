// ContentService.java
package com.example.pal.service;

import com.example.pal.dto.ContentDTO;
import com.example.pal.dto.ContentUploadDTO;
import com.example.pal.model.Content;
import com.example.pal.model.Course;
import com.example.pal.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    private final String UPLOAD_DIR = "uploads/";

    public ContentDTO uploadContent(ContentUploadDTO contentUploadDTO) throws IOException {
        MultipartFile file = contentUploadDTO.getFile();
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.write(path, file.getBytes());

        Content content = new Content();
        content.setFileUrl(path.toString());
        content.setType(contentUploadDTO.getType());
        // Aquí debes asignar el curso, que debería estar en contentUploadDTO
        content.setCourse(new Course().setId(contentUploadDTO.getCourseId()));  // Establece el curso de acuerdo a tu lógica

        content = contentRepository.save(content);


        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setId(content.getId());
        contentDTO.setCourseId(content.getCourse().getId());
        contentDTO.setType(content.getType());
        contentDTO.setFileUrl(content.getFileUrl());

        return contentDTO;
    }

    public List<ContentDTO> getAllContent() {
        List<Content> contentList = contentRepository.findAll();
        // Convertir a DTO antes de devolverlo
    }

    public ContentDTO getContentById(Long id) {
        Optional<Content> contentOpt = contentRepository.findById(id);
        // Manejar el caso si no se encuentra el contenido
    }

    public void updateContent(Long id, ContentUploadDTO contentUploadDTO) throws IOException {
        Optional<Content> contentOpt = contentRepository.findById(id);
        if (contentOpt.isPresent()) {
            Content content = contentOpt.get();
            MultipartFile file = contentUploadDTO.getFile();
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, file.getBytes());

            content.setFileUrl(path.toString());
            content.setType(contentUploadDTO.getType());
            // Actualiza el curso si es necesario
            contentRepository.save(content);
        }
    }

    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }
}
