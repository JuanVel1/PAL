package com.example.pal.dto;
import java.util.Set;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<RoleDTO> roles;
}
