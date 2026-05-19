package user_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombreApellido;


    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nombrePantalla;

    @Email(message = "Debe ser un correo electrónico válido")
    @NotBlank(message = "El correo no puede estar vacío")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$",
            message = "El correo debe pertenecer al dominio @gmail.com"
    )
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false)
    private String password;

    @DecimalMin(value = "0.0", message = "La billetera no puede tener saldo negativo")
    @Column(nullable = false)
    private BigDecimal billetera = BigDecimal.ZERO;

    @Column(name = "cuenta_bloqueada")
    private Boolean cuentaBloqueada = false;

    @Column(name = "anio_registro")
    private Long anioRegistro;

}