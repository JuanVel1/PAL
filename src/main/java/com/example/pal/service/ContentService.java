// ContentService.java
package com.example.pal.service;

import com.example.pal.dto.ContentDTO;
import com.example.pal.model.Content;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.example.pal.repository.ContentRepository;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;

    private final ModelMapper modelMapper;

    public ContentService(ContentRepository contentRepository, ModelMapper modelMapper) {
        this.contentRepository = contentRepository;
        this.modelMapper = modelMapper;
    }

    public ContentDTO uploadContent(ContentDTO contentDTO) {
        Content content = modelMapper.map(contentDTO, Content.class);
        Content savedContent = contentRepository.save(content);
        return modelMapper.map(savedContent, ContentDTO.class);
    }

    public List<ContentDTO> getAllContent() {
        return contentRepository.findAll()
                .stream()
                .map(content -> modelMapper.map(content, ContentDTO.class))
                .collect(Collectors.toList());
    }

    public ContentDTO getContentById(Long id) {
        return contentRepository.findById(id)
                .map(content -> modelMapper.map(content, ContentDTO.class))
                .orElseThrow(() -> new RuntimeException("Content not found with ID: " + id));
    }

    public ContentDTO updateContent(Long id, ContentDTO contentDTO) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with ID: " + id));

        content.setType(contentDTO.getType());
        content.setFileUrl(contentDTO.getFileUrl());

        return modelMapper.map(contentRepository.save(content), ContentDTO.class);
    }

    public void deleteContent(Long id) {
        if (!contentRepository.existsById(id)) {
            throw new RuntimeException("Content not found with ID: " + id);
        }
        contentRepository.deleteById(id);
    }
}
