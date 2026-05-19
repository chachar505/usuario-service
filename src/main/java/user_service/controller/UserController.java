package user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import user_service.dto.LoginRequest;
import user_service.dto.UserResponseDTO;
import user_service.exception.ResourceNotFoundException;
import user_service.model.User;
import user_service.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.autenticar(loginRequest)
                .map(user -> ResponseEntity.ok("Login exitoso. Bienvenido " + user.getNombrePantalla()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contrasena incorrectos"));
    }

    @PostMapping("/registro")
    public ResponseEntity<UserResponseDTO> registrar(@Valid @RequestBody User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToResponse(userService.registrarUsuario(usuario)));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listar() {
        return ResponseEntity.ok(
                userService.obtenerTodos().stream()
                        .map(this::mapToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> obtenerPorId(@PathVariable Long id) {
        return userService.obtenerPorId(id)
                .map(user -> ResponseEntity.ok(mapToResponse(user)))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody User detallesUsuario) {
        return ResponseEntity.ok(mapToResponse(userService.actualizarUsuario(id, detallesUsuario)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        userService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Convierte User (entidad) a UserResponseDTO (sin exponer password)
    private UserResponseDTO mapToResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .nombreApellido(user.getNombreApellido())
                .nombrePantalla(user.getNombrePantalla())
                .email(user.getEmail())
                .billetera(user.getBilletera())
                .cuentaBloqueada(user.getCuentaBloqueada())
                .anioRegistro(user.getAnioRegistro())
                .build();
    }
}