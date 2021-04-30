package com.org.dsb.pontoeletronico;

import com.org.dsb.pontoeletronico.entities.Empresa;
import com.org.dsb.pontoeletronico.entities.Funcionario;
import com.org.dsb.pontoeletronico.repositories.EmpresaRepository;
import com.org.dsb.pontoeletronico.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.org.dsb.pontoeletronico.enums.PerfilEnum.ROLE_ADMIN;
import static com.org.dsb.pontoeletronico.enums.PerfilEnum.ROLE_USUARIO;
import static com.org.dsb.pontoeletronico.utils.PasswordUtils.gerarBCrypt;

@SpringBootApplication
public class PontoeletronicoApplication {

    @Autowired
    private FuncionarioRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            try {
                Empresa empresa = empresaRepository
                        .findById(1l)
                        .get();

                Funcionario usuario = Funcionario
                        .builder()
                        .nome("usuario usuario")
                        .email("usuario@mail.com")
                        .cpf("24802933606")
                        .senha(gerarBCrypt("usuario"))
                        .perfil(ROLE_USUARIO)
                        .empresa(empresa)
                        .build();

                repository.save(usuario);

                Funcionario admin = Funcionario
                        .builder()
                        .nome("admin admin")
                        .email("admin@mail.com")
                        .cpf("18036181401")
                        .senha(gerarBCrypt("admin"))
                        .perfil(ROLE_ADMIN)
                        .empresa(empresa)
                        .build();

                repository.save(admin);
            } catch (Exception e) {}
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(PontoeletronicoApplication.class, args);
    }

}
