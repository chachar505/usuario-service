package user_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import user_service.dto.LoginRequest;
import user_service.model.User;
import user_service.repsoitory.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> autenticar(LoginRequest request) {
        // 1. Buscamos al usuario por el email del DTO
        return userRepository.findByEmail(request.email())
                // 2. Filtramos: solo si la contraseña coincide
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()));
    }

    public User registrarUsuario(User usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return userRepository.save(usuario);
    }


    public List<User> obtenerTodos() {
        return userRepository.findAll();
    }

    public Optional<User> obtenerPorId(Long id) {
        return userRepository.findById(id);
    }

    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);
    }

    public User actualizarUsuario(Long id, User detallesUsuario) {
        return userRepository.findById(id).map(usuario -> {
            usuario.setNombreApellido(detallesUsuario.getNombreApellido());
            usuario.setNombrePantalla(detallesUsuario.getNombrePantalla());

            // LOGICA PARA LA CONTRASEÑA
            // 1. Verificamos si se envió una contraseña nueva (que no sea nula ni vacía)
            if (detallesUsuario.getPassword() != null && !detallesUsuario.getPassword().isBlank()) {
                // 2. Si envió una nueva, la encriptamos antes de guardarla
                usuario.setPassword(passwordEncoder.encode(detallesUsuario.getPassword()));
            }
            // Si no envió nada en el campo password, se queda la que ya estaba en la BD

            return userRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

}
