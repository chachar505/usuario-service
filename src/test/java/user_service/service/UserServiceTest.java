package user_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import user_service.dto.LoginRequest;
import user_service.exception.ResourceNotFoundException;
import user_service.model.User;
import user_service.repsoitory.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new User();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setNombreApellido("Juan Perez");
        usuarioEjemplo.setNombrePantalla("JuanitoGamer");
        usuarioEjemplo.setEmail("juan.perez@gmail.com");
        usuarioEjemplo.setPassword("hashedPassword");
        usuarioEjemplo.setBilletera(new BigDecimal("500.00"));
        usuarioEjemplo.setCuentaBloqueada(false);
        usuarioEjemplo.setAnioRegistro(2024L);
    }

    @Test
    @DisplayName("Login exitoso con credenciales correctas")
    void autenticar_credencialesCorrectas_retornaUsuario() {
        LoginRequest request = new LoginRequest("juan.perez@gmail.com", "pass123456");
        when(userRepository.findByEmail("juan.perez@gmail.com"))
                .thenReturn(Optional.of(usuarioEjemplo));
        when(passwordEncoder.matches("pass123456", "hashedPassword"))
                .thenReturn(true);

        Optional<User> resultado = userService.autenticar(request);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombrePantalla()).isEqualTo("JuanitoGamer");
    }

    @Test
    @DisplayName("Login fallido con contraseña incorrecta")
    void autenticar_contrasenaIncorrecta_retornaVacio() {
        LoginRequest request = new LoginRequest("juan.perez@gmail.com", "wrongpass");
        when(userRepository.findByEmail("juan.perez@gmail.com"))
                .thenReturn(Optional.of(usuarioEjemplo));
        when(passwordEncoder.matches("wrongpass", "hashedPassword"))
                .thenReturn(false);

        Optional<User> resultado = userService.autenticar(request);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Registrar usuario guarda con contraseña encriptada")
    void registrarUsuario_guardaConPasswordEncriptada() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(usuarioEjemplo);

        User resultado = userService.registrarUsuario(usuarioEjemplo);

        assertThat(resultado).isNotNull();
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Obtener todos los usuarios retorna lista")
    void obtenerTodos_retornaLista() {
        when(userRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        List<User> resultado = userService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("juan.perez@gmail.com");
    }

    @Test
    @DisplayName("Eliminar usuario inexistente lanza excepción")
    void eliminarUsuario_noExiste_lanzaExcepcion() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.eliminarUsuario(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Actualizar usuario existente lo guarda correctamente")
    void actualizarUsuario_existente_guardaCambios() {
        User nuevoDato = new User();
        nuevoDato.setNombreApellido("Juan Modificado");
        nuevoDato.setNombrePantalla("JuanitoV2");
        nuevoDato.setPassword("nuevaPass123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));
        when(passwordEncoder.encode("nuevaPass123")).thenReturn("newHash");
        when(userRepository.save(any(User.class))).thenReturn(usuarioEjemplo);

        User resultado = userService.actualizarUsuario(1L, nuevoDato);

        assertThat(resultado).isNotNull();
        verify(userRepository).save(any(User.class));
    }
}