package com.example.pal.config;

import org.springframework.lang.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/autenticacion/**", "/oauth2/**", "/login/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2 
                    .defaultSuccessUrl("http://localhost:3000/inicio", true)
                    .failureUrl("http://localhost:3000/login?error=true"))
                .headers(headers -> headers
                    .contentSecurityPolicy(csp -> csp
                        .policyDirectives("frame-ancestors 'self' https://dominio-confiable.com;")));
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Configuration
    public static class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(@NonNull CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173", "http://localhost:3000")  // Permitir solicitudes desde el frontend
                    .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos permitidos
                    .allowedHeaders("Content-Type", "Authorization");  // Cabeceras permitidas
        }
    }


}