package user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.assemblers.UserModelAssembler;
import user_service.dto.LoginRequest;
import user_service.dto.UserResponseDTO;
import user_service.exception.ResourceNotFoundException;
import user_service.model.User;
import user_service.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios V2 (HATEOAS)", description = "Metodos del microservicio usuario con HATEOAS")
public class UserControllerV2 {

    private final UserService userService;
    private final UserModelAssembler assembler;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Se le pide al usuario su gmail y contraseña para entrar")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.autenticar(loginRequest)
                .map(user -> ResponseEntity.ok("Login exitoso. Bienvenido " + user.getNombrePantalla()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contrasena incorrectos"));
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar por primera vez a un usuario", description = "Registra y guarda un nuevo usuario en la base de datos")
    public ResponseEntity<EntityModel<UserResponseDTO>> registrar(@Valid @RequestBody User usuario) {
        User guardado = userService.registrarUsuario(usuario);
        return ResponseEntity
                .created(linkTo(methodOn(UserControllerV2.class).obtenerPorId(guardado.getId())).toUri())
                .body(assembler.toModel(guardado));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Lista de todos los usuarios", description = "Lista con todos los usuarios registrados en la base de datos")
    public CollectionModel<EntityModel<UserResponseDTO>> listar() {
        List<EntityModel<UserResponseDTO>> usuarios = userService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios,
                linkTo(methodOn(UserControllerV2.class).listar()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar usuario por id", description = "Busca al usuario por su id")
    public EntityModel<UserResponseDTO> obtenerPorId(
            @Parameter(description = "se coloca el id del usuario para buscarlo, ejemplo: 1", required = true)
            @PathVariable Long id) {
        User usuario = userService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el id: " + id));
        return assembler.toModel(usuario);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente buscandolo por su id")
    public EntityModel<UserResponseDTO> actualizar(
            @Parameter(description = "se coloca el id del usuario para actualizaro (ejemplo: 1), luego se colocan el resto de parametros", required = true)
            @PathVariable Long id,
            @Valid @RequestBody User detallesUsuario) {
        User actualizado = userService.actualizarUsuario(id, detallesUsuario);
        return assembler.toModel(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario de la base de datos buscandolo por su ID")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "se coloca el id del usuario para eliminarlo, ejemplo: 1", required = true)
            @PathVariable Long id) {
        userService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}