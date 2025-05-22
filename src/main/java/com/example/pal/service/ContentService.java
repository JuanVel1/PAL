// ContentService.java
package com.example.pal.service;

import com.example.pal.dto.ContentDTO;
import com.example.pal.model.Content;
import com.example.pal.model.Course; 
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import com.example.pal.repository.ContentRepository;
import com.example.pal.dto.ResponseDTO;
import com.example.pal.service.FileStorageService;

@Service
public class ContentService {
    @Autowired
    private final ContentRepository contentRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FileStorageService fileStorageService;

    private final ModelMapper modelMapper;

    public ContentService(ContentRepository contentRepository, ModelMapper modelMapper) {
        this.contentRepository = contentRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<ResponseDTO<ContentDTO>> createContent(ContentDTO contentDTO) {
        try {
            // Convertir ContentDTO a entidad Content
            Content content = modelMapper.map(contentDTO, Content.class);

            Optional<Course> course = courseService.getCourseById(contentDTO.getCourseId());

            if (course.isEmpty()) {
                ResponseDTO<ContentDTO> response = new ResponseDTO<>("Course not found", null);
                return ResponseEntity.status(404).body(response);
            }

            content.setCourse(course.get());

            Content savedContent = contentRepository.save(content);

            ContentDTO createdContentDTO = modelMapper.map(savedContent, ContentDTO.class);
            ResponseDTO<ContentDTO> response = new ResponseDTO<>("Content created successfully", createdContentDTO);

            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            ResponseDTO<ContentDTO> response = new ResponseDTO<>(e.getMessage(), null);
            return ResponseEntity.status(400).body(response);
        }
    }

    // Obtener todos los contenidos
    public ResponseEntity<ResponseDTO<ContentDTO>> getAllContent() {
        List<ContentDTO> contentList = contentRepository.findAll()
                .stream()
                .map(content -> modelMapper.map(content, ContentDTO.class))
                .collect(Collectors.toList());

        ResponseDTO<ContentDTO> response = new ResponseDTO<>("Contents retrieved successfully", contentList);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ResponseDTO<ContentDTO>> getContentById(Long id) {
        Optional<Content> content = contentRepository.findById(id);

        if (content.isEmpty()) {
            ResponseDTO<ContentDTO> response = new ResponseDTO<>("Content not found with ID: " + id, null);
            return ResponseEntity.status(404).body(response);
        }

        ContentDTO contentDTO = modelMapper.map(content.get(), ContentDTO.class);
        
        // Si existe un archivo asociado, incluir el archivo directamente
        if (content.get().getNameFile() != null) {
            try {
                Path filePath = fileStorageService.getFilePath(content.get().getNameFile());
                byte[] fileBytes = Files.readAllBytes(filePath);
                contentDTO.setFileBytes(fileBytes);
            } catch (IOException e) {
                // Si hay error al leer el archivo, continuar sin él
                System.err.println("Error reading file: " + e.getMessage());
            }
        }

        ResponseDTO<ContentDTO> response = new ResponseDTO<>("Content retrieved successfully", contentDTO);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ResponseDTO<ContentDTO>> updateContent(Long id, ContentDTO contentDTO) {
        Optional<Content> existingContent = contentRepository.findById(id);

        if (existingContent.isEmpty()) {
            ResponseDTO<ContentDTO> response = new ResponseDTO<>("Content not found with ID: " + id, null);
            return ResponseEntity.status(404).body(response);
        }

        Content content = existingContent.get();
        content.setFileUrl(contentDTO.getFileUrl());
        content.setType(contentDTO.getType());

        Content updatedContent = contentRepository.save(content);
        ContentDTO updatedContentDTO = modelMapper.map(updatedContent, ContentDTO.class);
        ResponseDTO<ContentDTO> response = new ResponseDTO<>("Content updated successfully", updatedContentDTO);
        return ResponseEntity.ok(response);
    }

    // Eliminar contenido
    public ResponseEntity<ResponseDTO<Void>> deleteContent(Long id) {
        if (!contentRepository.existsById(id)) {
            ResponseDTO<Void> response = new ResponseDTO<>("Content not found with ID: " + id, null);
            return ResponseEntity.status(404).body(response);
        }

        contentRepository.deleteById(id);
        ResponseDTO<Void> response = new ResponseDTO<>("Content deleted successfully", null);
        return ResponseEntity.status(204).body(response);  // 204 No Content
    }

    public ResponseEntity<ResponseDTO<ContentDTO>> findByCourseId(Long courseId) {
        List<ContentDTO> contentList = contentRepository.findByCourseId(courseId)
                .stream()
                .map(content -> modelMapper.map(content, ContentDTO.class))
                .collect(Collectors.toList());

        ResponseDTO<ContentDTO> response = new ResponseDTO<>("Contents retrieved successfully", contentList);
        return ResponseEntity.status(200).body(response);
    }
}
