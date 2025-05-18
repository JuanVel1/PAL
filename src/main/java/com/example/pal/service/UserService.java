package com.example.pal.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pal.dto.CreateUserDTO;
import com.example.pal.dto.UserDTO;
import com.example.pal.model.Role;
import com.example.pal.model.User;
import com.example.pal.repository.RoleRepository;
import com.example.pal.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public User createUserWithRoles(CreateUserDTO userDTO) {

        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new RuntimeException("User already exists");
        }
        if (userDTO.getRoles() == null || userDTO.getRoles().length == 0) {
            throw new RuntimeException("User must have at least one role");
        }

        User user = new User();
        Set<Role> roles = new HashSet<>();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        /* user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());*/

        for (String roleName : userDTO.getRoles()) {
            String normalizedRoleName = roleName.trim().toLowerCase(); // Normaliza el nombre del rol
            Role role = roleRepository.findByName(normalizedRoleName)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(normalizedRoleName); // Asigna un String simple
                        return roleRepository.save(newRole);
                    });
            roles.add(role);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAllWithRoles();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByRole(String roleName) {
        // Normaliza el nombre del rol de la misma manera que en createUserWithRoles
        String normalizedRoleName = roleName.trim().toLowerCase();

        Role role = roleRepository.findByName(normalizedRoleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + normalizedRoleName));

        List<User> users = userRepository.findUsersByRoles(Set.of(role));

        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());

        // Solo actualiza la contraseña si se proporciona una nueva
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Actualiza los roles si se proporcionan
        if (userDetails.getRoles() != null && !userDetails.getRoles().isEmpty()) {
            user.setRoles(userDetails.getRoles());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
                                                                                          