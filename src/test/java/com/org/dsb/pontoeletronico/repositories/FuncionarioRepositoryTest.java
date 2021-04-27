package com.org.dsb.pontoeletronico.repositories;

import com.org.dsb.pontoeletronico.entities.Empresa;
import com.org.dsb.pontoeletronico.entities.Funcionario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static com.org.dsb.pontoeletronico.enums.PerfilEnum.ROLE_USUARIO;
import static com.org.dsb.pontoeletronico.utils.PasswordUtils.gerarBCrypt;
import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String EMAIL = "email@email.com";
    private static final String CPF = "13145613199";
    private static final String CNPJ = "51463645000100";

    @BeforeEach
    public void setup() throws Exception {
        Empresa empresa = this
                .empresaRepository
                .save(obterDadosEmpresa());
        this
                .funcionarioRepository
                .save(obterDadosFuncionario(empresa));
    }

    @AfterEach
    public final void tearDown() throws Exception {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void buscarFuncionarioPorEmail() {
        Funcionario funcionario = this
                .funcionarioRepository
                .findByEmail(EMAIL);
        assertEquals(EMAIL, funcionario.getEmail());
    }

    @Test
    public void buscarFuncionarioPorCpf() {
        Funcionario funcionario = this
                .funcionarioRepository
                .findByCpf(CPF);
        assertEquals(CPF, funcionario.getCpf());
    }

    @Test
    public void buscarFuncionarioPorEmailECpf() {
        Funcionario funcionario = this
                .funcionarioRepository
                .findByCpfOrEmail(CPF, EMAIL);
        assertNotNull(funcionario);
    }

    @Test
    public void buscarFuncionarioPorEmailOuCpfParaEmailInvalido() {
        Funcionario funcionario = this
                .funcionarioRepository
                .findByCpfOrEmail(CPF, "email@invalido");
        assertNotNull(funcionario);
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) {
        return Funcionario
                .builder()
                .nome("Fulano Qualquer")
                .perfil(ROLE_USUARIO)
                .senha(gerarBCrypt("HereMyPassword"))
                .cpf(CPF)
                .email(EMAIL)
                .empresa(empresa)
                .dataCriacao(new Date())
                .dataAtualizacao(new Date())
                .qtdHorasAlmoco(1f)
                .qtdHorasTrabalhoDia(8f)
                .valorHora(valueOf(15))
                .build();
    }

    private Empresa obterDadosEmpresa() {
        return Empresa
                .builder()
                .razaoSocial("Empresa de Exemplo")
                .cnpj(CNPJ)
                .build();
    }
}
