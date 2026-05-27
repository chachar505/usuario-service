package user_service.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String nombreApellido;
    private String nombrePantalla;
    private String email;
    private BigDecimal billetera;
    private boolean cuentaBloqueada;
    private Long anioRegistro;
}