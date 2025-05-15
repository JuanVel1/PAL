package com.example.pal.controller;
import com.example.pal.dto.ContentDTO; 
import com.example.pal.dto.ResponseDTO;
import com.example.pal.service.ContentService; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; 
 

@RestController
@RequestMapping("/api/content")
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDTO<ContentDTO>> createContent(@RequestBody ContentDTO contentDTO) {
        return contentService.createContent(contentDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<ContentDTO>> getAllContent() {
        return contentService.getAllContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ContentDTO>> getContentById(@PathVariable Long id) {
        return contentService.getContentById(id);
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
}
