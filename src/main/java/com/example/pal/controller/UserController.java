package com.example.pal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pal.service.UserService;
import com.example.pal.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String[] roles) {
        User user = userService.createUserWithRoles(username, password, roles);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
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
