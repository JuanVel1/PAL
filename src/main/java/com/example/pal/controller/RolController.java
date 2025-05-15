package com.example.pal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.pal.service.RolService;
import com.example.pal.model.Role;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    @Autowired
    private RolService rolService;

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestParam("roleName") String roleName) {
        Role role = rolService.createRol(roleName);
        return ResponseEntity.status(201).body(role);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles(){
        return ResponseEntity.ok(rolService.getAllRoles());
    }
}
