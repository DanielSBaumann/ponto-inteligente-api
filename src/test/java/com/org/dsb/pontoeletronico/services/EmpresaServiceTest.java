package com.org.dsb.pontoeletronico.services;

import com.org.dsb.pontoeletronico.entities.Empresa;
import com.org.dsb.pontoeletronico.repositories.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
public class EmpresaServiceTest {

    @MockBean
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaService empresaService;

    private static final String CNPJ = "51463645000100";

    @BeforeEach
    public void setup() {
        given(this.empresaRepository
                .findByCnpj(anyString()))
                .willReturn(new Empresa());
        given(this.empresaRepository
                .save(any(Empresa.class)))
                .willReturn(new Empresa());
    }

    @Test
    public void buscarEmpresaPorCnpj() throws Exception {
        Optional<Empresa> empresa = this.empresaService
                .buscarPorCnpj(CNPJ);
        assertTrue(empresa.isPresent());
    }

    @Test
    public void persistirEmpresa() throws Exception {
        Empresa empresa = this.empresaService
                .persistir(new Empresa());
        assertNotNull(empresa);
    }
}
















