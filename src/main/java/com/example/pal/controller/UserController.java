package com.example.pal.controller;

import java.util.List;
import java.util.Optional;
import com.example.pal.dto.CreateUserDTO;
import com.example.pal.dto.ResponseDTO;
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
    public ResponseEntity<ResponseDTO<User>> createUser(@ModelAttribute CreateUserDTO userDTO) {
        try{
            User user = userService.createUserWithRoles(userDTO);
            ResponseDTO<User> response = new ResponseDTO<>("User created successfully", user);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            ResponseDTO<User> response = new ResponseDTO<>(e.getMessage(), null);
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<UserDTO>> getAllUsers(){
        List<UserDTO> users = userService.getAllUsers();
        ResponseDTO<UserDTO> response = new ResponseDTO<>("Users retrieved successfully", users);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<User>> getUserById(@PathVariable Long id){
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            ResponseDTO<User> response = new ResponseDTO<>("User not found", null);
            return ResponseEntity.status(404).body(response);
        }
        ResponseDTO<User> response = new ResponseDTO<>("User retrieved successfully", user.get());
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO<User>> updateUser(@PathVariable Long id, @RequestBody User userDetails){
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            ResponseDTO<User> response = new ResponseDTO<>("User updated successfully", updatedUser);
            return ResponseEntity.status(200).body(response);
        } else {
            ResponseDTO<User> response = new ResponseDTO<>("User not found", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
    	userService.deleteUser(id);
    	return ResponseEntity.noContent().build();
    }
}
