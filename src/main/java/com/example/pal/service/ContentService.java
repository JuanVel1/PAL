// ContentService.java
package com.example.pal.service;

import com.example.pal.dto.ContentDTO;
import com.example.pal.model.Content;
import com.example.pal.model.Course;
import com.example.pal.repository.CourseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;

import com.example.pal.repository.ContentRepository;
import com.example.pal.dto.ResponseDTO;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private CourseService courseService;

    private final ModelMapper modelMapper;

    public ContentService(ContentRepository contentRepository, ModelMapper modelMapper) {
        this.contentRepository = contentRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<ResponseDTO<ContentDTO>> createContent(ContentDTO contentDTO) {
        try {
            // Convertir ContentDTO a entidad Content
            Content content = modelMapper.map(contentDTO, Content.class);

            Optional<Course> course = courseService.getCourseById(content.getCourse().getId());

            if (course.isEmpty()) {
                ResponseDTO<ContentDTO> response = new ResponseDTO<>("Course not found", null);
                return ResponseEntity.status(404).body(response);
            }

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
        content.setType(contentDTO.getType());
        content.setFileUrl(contentDTO.getFileUrl());

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
}
