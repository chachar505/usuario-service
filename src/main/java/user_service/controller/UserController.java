package user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuarios", description = "Metodos del microservicio usuario")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Se le pide al usuario su gmail y contraseña para entrar"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(example = "Login exitoso. Bienvenido NombreUsuario"))),
            @ApiResponse(responseCode = "401", description = "Email o contraseña incorrectos",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(example = "Email o contrasena incorrectos")))
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.autenticar(loginRequest)
                .map(user -> ResponseEntity.ok("Login exitoso. Bienvenido " + user.getNombrePantalla()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contrasena incorrectos"));
    }

    @PostMapping("/registro")
    @Operation(
            summary = "Registrar por primera vez a un usuario",
            description = "Registra y guarda un nuevo usuario en la base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Campo vacio o no valido",
                    content = @Content)
    })
    public ResponseEntity<UserResponseDTO> registrar(@Valid @RequestBody User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToResponse(userService.registrarUsuario(usuario)));
    }

    @GetMapping
    @Operation(
            summary = "Lista de todos los usuarios",
            description = "Lista con todos los usuarios registrados en la base de datos "
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
    public ResponseEntity<List<UserResponseDTO>> listar() {
        return ResponseEntity.ok(
                userService.obtenerTodos().stream()
                        .map(this::mapToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuario por id",
            description = "Busca al usuario por su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content)
    })
    public ResponseEntity<UserResponseDTO> obtenerPorId(
            @Parameter(description = "se coloca el id del usuario para buscarlo, ejemplo: 1", required = true)
            @PathVariable Long id) {
        return userService.obtenerPorId(id)
                .map(user -> ResponseEntity.ok(mapToResponse(user)))
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el id: " + id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente buscandolo por su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o campo vacio",
                    content = @Content)
    })
    public ResponseEntity<UserResponseDTO> actualizar(
            @Parameter(description = "se coloca el id del usuario para actualizaro (ejemplo: 1), luego se colocan el resto de parametros", required = true)
            @PathVariable Long id,
            @Valid @RequestBody User detallesUsuario) {
        return ResponseEntity.ok(mapToResponse(userService.actualizarUsuario(id, detallesUsuario)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario de la base de datos buscandolo por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content)
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "se coloca el id del usuario para eliminarlo, ejemplo: 1", required = true)
            @PathVariable Long id) {
        userService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

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