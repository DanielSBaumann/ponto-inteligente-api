package com.org.dsb.pontoeletronico;

import com.org.dsb.pontoeletronico.security.entities.Usuario;
import com.org.dsb.pontoeletronico.security.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import static com.org.dsb.pontoeletronico.enums.PerfilEnum.ROLE_ADMIN;
import static com.org.dsb.pontoeletronico.enums.PerfilEnum.ROLE_USUARIO;
import static com.org.dsb.pontoeletronico.utils.PasswordUtils.gerarBCrypt;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PontoeletronicoApplication {

    @Autowired
    private UsuarioRepository repository;

    @Bean
    public CommandLineRunner init() {
        return args -> {

            Usuario usuario = Usuario
                    .builder()
                    .email("usuario@mail.com")
                    .senha(gerarBCrypt("usuario"))
                    .perfil(ROLE_USUARIO)
                    .build();

            repository.save(usuario);

            Usuario admin = Usuario
                    .builder()
                    .email("admin@mail.com")
                    .senha(gerarBCrypt("admin"))
                    .perfil(ROLE_ADMIN)
                    .build();

            repository.save(admin);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(PontoeletronicoApplication.class, args);
    }

}
