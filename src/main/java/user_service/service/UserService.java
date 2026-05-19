package user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import user_service.dto.LoginRequest;
import user_service.exception.ResourceNotFoundException;
import user_service.model.User;
import user_service.repsoitory.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> autenticar(LoginRequest request) {
        log.info("Intentando autenticar usuario: {}", request.email());
        return userRepository.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()));
    }

    public User registrarUsuario(User usuario) {
        log.info("Registrando nuevo usuario: {}", usuario.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        User guardado = userRepository.save(usuario);
        log.info("Usuario registrado con id: {}", guardado.getId());
        return guardado;
    }

    public List<User> obtenerTodos() {
        log.info("Obteniendo lista de todos los usuarios");
        return userRepository.findAll();
    }

    public Optional<User> obtenerPorId(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return userRepository.findById(id);
    }

    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
        log.info("Usuario con id {} eliminado correctamente", id);
    }

    public User actualizarUsuario(Long id, User detallesUsuario) {
        log.info("Actualizando usuario con id: {}", id);
        return userRepository.findById(id).map(usuario -> {
            usuario.setNombreApellido(detallesUsuario.getNombreApellido());
            usuario.setNombrePantalla(detallesUsuario.getNombrePantalla());
            if (detallesUsuario.getPassword() != null && !detallesUsuario.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(detallesUsuario.getPassword()));
            }
            User actualizado = userRepository.save(usuario);
            log.info("Usuario con id {} actualizado correctamente", id);
            return actualizado;
        }).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }
}