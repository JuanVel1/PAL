package com.example.pal.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateUserDTO {
    private String username;
    private String password;
    private String email;
    private List<String> roles; // Cambiar a List<String>
}
