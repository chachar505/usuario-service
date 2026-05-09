package user_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user_service.model.User;
import user_service.repsoitory.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registrarUsuario(User usuario) {
        //  lógica de encriptar contraseña en el futuro
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
            usuario.setPassword(detallesUsuario.getPassword());

            return userRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

}
