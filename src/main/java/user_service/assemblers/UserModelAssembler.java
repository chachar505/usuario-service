package user_service.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import user_service.dto.UserResponseDTO;
import user_service.model.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import user_service.controller.UserControllerV2;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<UserResponseDTO>> {

    @Override
    public EntityModel<UserResponseDTO> toModel(User user) {
        UserResponseDTO dto = toDto(user);

        Link selfLink = linkTo(methodOn(UserControllerV2.class)
                .obtenerPorId(user.getId())).withSelfRel();

        Link listLink = Link.of("/api/v2/usuarios").withRel("usuarios");

        return EntityModel.of(dto, selfLink, listLink);
    }

    private UserResponseDTO toDto(User user) {
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