package com.example.pal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

@RestController 
@RequestMapping("/autenticacion")
public class AuthController {
    
    @GetMapping("/google")
    public RedirectView google() {
        // Redirecciona al endpoint de OAuth2 de Spring Security para Google
        return new RedirectView("/oauth2/authorization/google");
    }

    @GetMapping("/github")
    public RedirectView github() {
        // Redirecciona al endpoint de OAuth2 de Spring Security para GitHub
        return new RedirectView("/oauth2/authorization/github");
    }
    
@GetMapping("/success")
 public String success(@AuthenticationPrincipal OAuth2User user) {
     if (user != null) {
        String userName = getUserName(user);
        return "Autenticación exitosa para: " + userName;
     }
     return "No autenticado";
 }

private String getUserName(OAuth2User user) {
    // Try different attribute names based on OAuth2 provider
    String name = user.getAttribute("name");
    if (name == null) {
        name = user.getAttribute("login"); // GitHub
    }
    if (name == null) {
        name = user.getAttribute("email");
    }
    return name != null ? name : "Usuario";
}
    
    @GetMapping("/oauth2-info")
    public ResponseEntity<Map<String, Object>> getOAuth2Info(HttpServletRequest request) {
        Map<String, Object> info = new HashMap<>();
        
        String email = (String) request.getSession().getAttribute("oauth2_email");
        String name = (String) request.getSession().getAttribute("oauth2_name");
        String provider = (String) request.getSession().getAttribute("oauth2_provider");
        
        if (email != null) {
            info.put("email", email);
            info.put("name", name);
            info.put("provider", provider);
            
            // Limpiar la sesión después de obtener la información
            request.getSession().removeAttribute("oauth2_email");
            request.getSession().removeAttribute("oauth2_name");
            request.getSession().removeAttribute("oauth2_provider");
            
            return ResponseEntity.ok(info);
        }
        
        return ResponseEntity.notFound().build();
    }
}