package user_service.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import user_service.model.User;
import user_service.repsoitory.UserRepository;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info(">>> Usuarios ya cargados. Se omite la inicialización.");
            return;
        }


        User user1 = new User();
        user1.setNombreApellido("Juan Perez");
        user1.setNombrePantalla("JuanitoGamer");
        user1.setEmail("juan.perez@gmail.com");
        user1.setPassword(passwordEncoder.encode("pass123456"));
        user1.setBilletera(new BigDecimal("500.00"));
        user1.setCuentaBloqueada(false);
        user1.setAnioRegistro(2024L);



        User user2 = new User();
        user2.setNombreApellido("Maria Garcia");
        user2.setNombrePantalla("MariaPro");
        user2.setEmail("m.garcia@gmail.com");
        user2.setPassword(passwordEncoder.encode("maria.2024"));
        user2.setBilletera(new BigDecimal("100.00"));
        user2.setCuentaBloqueada(false);
        user2.setAnioRegistro(2023L);


        User user3 = new User();
        user3.setNombreApellido("Satoshi Nakamoto");
        user3.setNombrePantalla("CryptoKing");
        user3.setEmail("satoshi@gmail.com");
        user3.setPassword(passwordEncoder.encode("bitcoingod"));
        user3.setBilletera(new BigDecimal("9999.99"));
        user3.setCuentaBloqueada(false);
        user3.setAnioRegistro(2021L);


        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        log.info(">>> 3 usuarios cargados OK.");

    }


}
