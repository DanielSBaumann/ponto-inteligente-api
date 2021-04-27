package com.org.dsb.pontoeletronico.services;

import com.org.dsb.pontoeletronico.entities.Funcionario;
import com.org.dsb.pontoeletronico.repositories.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
public class FucionarioServiceTest {

    @MockBean
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioService funcionarioService;

    @BeforeEach
    public void setup() {
        given(this.funcionarioRepository
                .save(any(Funcionario.class)))
                .willReturn(new Funcionario());
        given(this.funcionarioRepository
                .findById(anyLong()))
                .willReturn(Optional.of(new Funcionario()));
        given(this.funcionarioRepository
                .findByEmail(anyString()))
                .willReturn(new Funcionario());
        given(this.funcionarioRepository
                .findByCpf(anyString()))
                .willReturn(new Funcionario());
    }

    @Test
    public void persistirFuncionario() {
        Funcionario funcionario = this.funcionarioService
                .persistir(new Funcionario());
        assertNotNull(funcionario);
    }

    @Test
    public void buscarFuncionarioPorId() {
        Optional<Funcionario> funcionario = this.funcionarioService
                .buscarPorId(1l);
        assertTrue(funcionario.isPresent());
    }

    @Test
    public void buscarFuncionarioPorEmail() {
        Optional<Funcionario> funcionario = this.funcionarioService
                .buscarPorEmail("email@test.com");
        assertTrue(funcionario.isPresent());
    }

    @Test
    public void buscarFuncionarioPorCpf() {
        Optional<Funcionario> funcionario = this.funcionarioService
                .buscarPorCpf("13145685212");
        assertTrue(funcionario.isPresent());
    }
}
