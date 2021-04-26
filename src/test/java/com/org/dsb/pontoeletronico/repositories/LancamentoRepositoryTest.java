package com.org.dsb.pontoeletronico.repositories;

import com.org.dsb.pontoeletronico.entities.Empresa;
import com.org.dsb.pontoeletronico.entities.Funcionario;
import com.org.dsb.pontoeletronico.entities.Lancamento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static com.org.dsb.pontoeletronico.enums.PerfilEnum.ROLE_USUARIO;
import static com.org.dsb.pontoeletronico.enums.TipoEnum.INICIO_ALMOCO;
import static com.org.dsb.pontoeletronico.utils.PasswordUtils.gerarCrypt;
import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    private Long funcionarioId;
    private static final String EMAIL = "email@email.com";
    private static final String CPF = "13145613199";
    private static final String CNPJ = "51463645000100";

    @BeforeEach
    public void setup() throws Exception {

        Empresa empresa = this
                .empresaRepository
                .save(obterDadosEmpresa());

        Funcionario funcionario = this
                .funcionarioRepository
                .save(obterDadosFuncionario(empresa));

        this.funcionarioId = funcionario
                .getId();
        this.lancamentoRepository
                .save(obterDadosLancamentos(funcionario));
        this.lancamentoRepository
                .save(obterDadosLancamentos(funcionario));
    }

    @AfterEach
    public final void tearDown() throws Exception {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void buscarLancamentosPorFuncionarioId() {
        List<Lancamento> lancamentos = this.lancamentoRepository
                .findByFuncionarioId(funcionarioId);
        assertEquals(2, lancamentos.size());
    }

    @Test
    public void buscarLancamentosPorFuncionarioIdPaginado() {
        PageRequest page = PageRequest.of(0, 10);
        Page<Lancamento> lancamentos = this.lancamentoRepository
                .findByFuncionarioId(funcionarioId, page);
        assertEquals(2, lancamentos.getTotalElements());
    }

    private Lancamento obterDadosLancamentos(Funcionario funcionario) {
        return Lancamento
                .builder()
                .data(new Date())
                .dataCriacao(new Date())
                .dataAtualizacao(new Date())
                .descricao("Uma descricao qualquer")
                .localizacao("Location")
                .tipo(INICIO_ALMOCO)
                .funcionario(funcionario)
                .build();
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) {
        return Funcionario
                .builder()
                .nome("Fulano Qualquer")
                .perfil(ROLE_USUARIO)
                .senha(gerarCrypt("HereMyPassword"))
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
