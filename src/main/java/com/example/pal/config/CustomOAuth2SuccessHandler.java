package com.example.pal.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.pal.model.User;
import com.example.pal.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        // Obtener el email del usuario OAuth2
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        
        // Si no hay email, intentar obtenerlo de login (para GitHub)
        if (email == null) {
            email = oauth2User.getAttribute("login") + "@github.local";
        }
        
        // Verificar si el usuario ya existe en la base de datos
        User existingUser = userRepository.findByEmail(email);
        
        if (existingUser == null) {
            // Primera vez que inicia sesión - redirigir a crear usuario
            // Guardar información temporalmente en la sesión
            request.getSession().setAttribute("oauth2_email", email);
            request.getSession().setAttribute("oauth2_name", name);
            request.getSession().setAttribute("oauth2_provider", getProvider(authentication));
            
            response.sendRedirect("http://localhost:3000/usuarios/crear");
        } else {
            // Usuario ya existe - redirigir a la pagina de inicio
            response.sendRedirect("http://localhost:3000/inicio");
        }
    }
    
    private String getProvider(Authentication authentication) {
        // Determinar el proveedor OAuth2 (google, github, etc.)
        String clientRegistrationId = 
            (String) ((OAuth2User) authentication.getPrincipal())
                .getAttributes().get("client_registration_id");
        
        if (clientRegistrationId != null) {
            return clientRegistrationId;
        }
        
        // Fallback: intentar determinar por los atributos disponibles
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        if (user.getAttribute("avatar_url") != null) {
            return "github";
        } else if (user.getAttribute("picture") != null) {
            return "google";
        }
        
        return "unknown";
    }
} 