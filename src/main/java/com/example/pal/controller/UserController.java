package com.example.pal.controller;

import java.util.List;
import java.util.Optional;
import com.example.pal.dto.CreateUserDTO;

import com.example.pal.dto.UserDTO;

import com.example.pal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pal.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@ModelAttribute CreateUserDTO userDTO) {

        User user = userService.createUserWithRoles(userDTO);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
    	return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
    	Optional<User> user = userService.getUserById(id);
    	return user.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails){
    	User updatedUser = userService.updateUser(id, userDetails);
    	return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
    	userService.deleteUser(id);
    	return ResponseEntity.noContent().build();
    }
}
