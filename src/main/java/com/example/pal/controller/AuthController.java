package com.example.pal.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

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
            return "Autenticación exitosa para: " + user.getAttribute("name");
        }
        return "No autenticado";
    }
}