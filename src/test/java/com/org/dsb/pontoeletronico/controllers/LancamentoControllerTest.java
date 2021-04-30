package com.org.dsb.pontoeletronico.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.dsb.pontoeletronico.dtos.LancamentoDto;
import com.org.dsb.pontoeletronico.entities.Funcionario;
import com.org.dsb.pontoeletronico.entities.Lancamento;
import com.org.dsb.pontoeletronico.enums.TipoEnum;
import com.org.dsb.pontoeletronico.services.FuncionarioService;
import com.org.dsb.pontoeletronico.services.LancamentoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.org.dsb.pontoeletronico.enums.TipoEnum.INICIO_TRABALHO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LancamentoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LancamentoService lancamentoService;

    @MockBean
    private FuncionarioService funcionarioService;

    private static final String URL_BASE = "/api/lancamentos/";
    private static final Long ID_FUNCIONARIO = 1l;
    private static final Long ID_LANCAMENTO = 1l;
    private static final String TIPO = INICIO_TRABALHO.name();
    private static final Date DATA = new Date();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    @WithMockUser
    public void cadastrarLancamento() throws Exception {
        Lancamento lancamento = obterDadosLancamento();

        given(this.funcionarioService.buscarPorId(anyLong()))
                .willReturn(Optional.of(new Funcionario()));
        given(this.lancamentoService.persistir(any(Lancamento.class)))
                .willReturn(lancamento);

        mvc.perform(post(URL_BASE)
                .content(this.obterJsonRequisicaoPost())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID_LANCAMENTO))
                .andExpect(jsonPath("$.data.tipo").value(TIPO))
                .andExpect(jsonPath("$.data.data").value(this.dateFormat.format(DATA)))
                .andExpect(jsonPath("$.data.funcionarioId").value(ID_FUNCIONARIO))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithMockUser
    public void cadastrarLancamentoFuncionarioIdInvalido() throws Exception {
        given(this.funcionarioService.buscarPorId(anyLong()))
                .willReturn(Optional.empty());

        mvc.perform(post(URL_BASE)
                .content(this.obterJsonRequisicaoPost())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("Funcionario nao encontrado. ID inexistente"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@email.com", roles = {"ADMIN"})
    public void removerLancamento() throws Exception {
        given(this.lancamentoService.buscarPorId(anyLong()))
                .willReturn(Optional.of(new Lancamento()));
        mvc.perform(delete(URL_BASE + ID_LANCAMENTO)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void removerLancamentoNegado() throws Exception {
        given(this.lancamentoService.buscarPorId(anyLong()))
                .willReturn(Optional.of(new Lancamento()));
        mvc.perform(delete(URL_BASE + ID_LANCAMENTO)
                .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    private Lancamento obterDadosLancamento() {

        Lancamento lancamento = Lancamento
                .builder()
                .id(ID_LANCAMENTO)
                .data(DATA)
                .tipo(TipoEnum.valueOf(TIPO))
                .funcionario(new Funcionario())
                .build();

        lancamento.getFuncionario().setId(ID_FUNCIONARIO);

        return lancamento;
    }

    private String obterJsonRequisicaoPost() throws JsonProcessingException {
        LancamentoDto dto = LancamentoDto
                .builder()
                .id(null)
                .data(this.dateFormat.format(DATA))
                .tipo(TIPO)
                .funcionarioId(ID_FUNCIONARIO)
                .build();
        return new ObjectMapper().writeValueAsString(dto);
    }
}



























