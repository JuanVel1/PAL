package com.example.pal.service;

import com.example.pal.model.Role;
import com.example.pal.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRol(String rolName) {
        Role rol = new Role();
        rol.setName(rolName);
        return roleRepository.save(rol);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void deleteRol(Long id) {
        roleRepository.deleteById(id);
    }
}
