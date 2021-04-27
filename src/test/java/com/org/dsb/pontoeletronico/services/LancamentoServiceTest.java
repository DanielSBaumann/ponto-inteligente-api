package com.org.dsb.pontoeletronico.services;

import com.org.dsb.pontoeletronico.entities.Lancamento;
import com.org.dsb.pontoeletronico.repositories.LancamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceTest {

    @MockBean
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private LancamentoService lancamentoService;

    @BeforeEach
    public void setup() {
        given(this.lancamentoRepository
                .findByFuncionarioId(anyLong(), any(PageRequest.class)))
                .willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
        given(this.lancamentoRepository
                .findById(anyLong()))
                .willReturn(Optional.of(new Lancamento()));
        given(this.lancamentoRepository
                .save(any(Lancamento.class)))
                .willReturn(new Lancamento());
    }

    @Test
    public void buscarLancamentoPorFuncionarioId() {
        Page<Lancamento> lancamento = this.lancamentoService
                .buscarPorFuncionarioId(1l, PageRequest.of(0, 10));
        assertNotNull(lancamento);
    }

    @Test
    public void buscarLancamentoPorId() {
        Optional<Lancamento> lancamento = this.lancamentoService
                .buscarPorId(1l);
        assertTrue(lancamento.isPresent());
    }

    @Test
    public void persistirLancamento() {
        Lancamento lancamento = this.lancamentoService.persistir(new Lancamento());
        assertNotNull(lancamento);
    }
}
















