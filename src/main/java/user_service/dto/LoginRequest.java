package user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "Debe ser un correo válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password) {
}
