package com.backend.MonoPat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // Le dice a Spring que esta es una clase de configuración
@EnableWebSecurity // Habilita la seguridad web de Spring
public class SecurityConfig {

    @Bean // Define un "Bean", un objeto gestionado por Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // 1. Habilita la configuración de CORS que definiremos más abajo
                .csrf(csrf -> csrf.disable()) // 2. Deshabilita CSRF (importante para APIs REST stateless)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 3. Permite todas las peticiones a cualquier endpoint
                );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 4. Define los orígenes permitidos (¡IMPORTANTE!)
        // Aquí debes poner la URL de tu frontend. Si usas Live Server en VSCode, suele ser 5500.
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://localhost:5500"));

        // 5. Define los métodos HTTP permitidos (GET, POST, etc.)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 6. Define las cabeceras HTTP permitidas
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 7. Aplica esta configuración a todas las rutas
        return source;
    }
}

