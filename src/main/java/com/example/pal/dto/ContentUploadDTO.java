
package com.example.pal.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ContentUploadDTO {
    private MultipartFile file;
    private String type;
    private Long courseId;

}
