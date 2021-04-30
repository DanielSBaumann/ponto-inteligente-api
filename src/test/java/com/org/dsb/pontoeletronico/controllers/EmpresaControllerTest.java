package com.org.dsb.pontoeletronico.controllers;

import com.org.dsb.pontoeletronico.entities.Empresa;
import com.org.dsb.pontoeletronico.services.EmpresaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmpresaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmpresaService empresaService;

    private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/empresas/cnpj/";
    private static final Long ID = Long.valueOf(1);
    private static final String CNPJ = "51463645000100";
    private static final String RAZAO_SOCIAL = "Empresa XYZ";

    @Test
    @WithMockUser
    public void buscarEmpresaCnpjInvalido() throws Exception {
        given(this.empresaService
                .buscarPorCnpj(anyString()))
                .willReturn(empty());
        mvc.perform(get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ)
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors")
                        .value("Empresa não encontrada para o CNPJ " + CNPJ));
    }

    @Test
    @WithMockUser
    public void buscarEmpresaPorCnpjValido() throws Exception {
        given(this.empresaService
                .buscarPorCnpj(anyString()))
                .willReturn(Optional.of(this.obterDadosEmpresa()));
        mvc.perform(get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID))
                .andExpect(jsonPath("$.data.razaoSocial", equalTo(RAZAO_SOCIAL)))
                .andExpect(jsonPath("$.data.cnpj", equalTo(CNPJ)))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    private Empresa obterDadosEmpresa() {
        return Empresa
                .builder()
                .id(ID)
                .razaoSocial(RAZAO_SOCIAL)
                .cnpj(CNPJ)
                .build();
    }

}





















