package user_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import user_service.model.User;
import user_service.repsoitory.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info(">>> Usuarios ya cargados. Se omite la inicialización.");
            return;
        }

        Faker faker = new Faker();
        List<User> usuarios = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setNombreApellido(faker.name().fullName());
            user.setNombrePantalla(faker.internet().username());
            user.setEmail(faker.internet().emailAddress()
                    .replaceAll("@.*", "@gmail.com"));
            user.setPassword(passwordEncoder.encode(faker.internet().password(8, 16)));
            user.setBilletera(BigDecimal.valueOf(faker.number().randomDouble(2, 0, 1000))
                    .setScale(2, RoundingMode.HALF_UP));
            user.setCuentaBloqueada(false);
            user.setAnioRegistro((long) faker.number().numberBetween(2018, 2025));
            usuarios.add(user);
        }

        userRepository.saveAll(usuarios);
        log.info(">>> 10 usuarios generados con DataFaker OK.");
    }
}