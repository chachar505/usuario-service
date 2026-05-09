package user_service.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.model.User;
import user_service.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/registro")
    public ResponseEntity<User> registrar(@Valid @RequestBody User usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registrarUsuario(usuario));
    }

    @GetMapping("/lista")
    public List<User> listar() {
        return userService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> obtenerPorId(@PathVariable Long id) {
        return userService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (userService.obtenerPorId(id).isPresent()) {
            userService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> actualizar(@PathVariable Long id, @Valid @RequestBody User detallesUsuario) {
        try {
            User usuarioActualizado = userService.actualizarUsuario(id, detallesUsuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



}
