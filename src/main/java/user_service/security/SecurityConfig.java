package user_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivado porque usaremos Postman/APIs REST
                .authorizeHttpRequests(auth -> auth
                        // Permitimos el registro y el login público sin contraseña
                        .requestMatchers("/api/usuarios/registro", "/api/usuarios/login").permitAll()
                        // Cualquier otra petición (como listar usuarios) requerirá estar autenticado
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}
