package com.org.dsb.pontoeletronico.repositories;

import com.org.dsb.pontoeletronico.entities.Empresa;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {

    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String CNPJ = "51463645000100";

    @BeforeEach
    public void setup() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa de exemplo");
        empresa.setCnpj(CNPJ);
        this.empresaRepository.save(empresa);
    }

    @AfterEach
    public final void tearDown() throws Exception{
        this.empresaRepository.deleteAll();
    }

    @Test
    public void buscarEmpresaPorCNPJ() {
        Empresa empresa = this
                .empresaRepository
                .findByCnpj(CNPJ);
        assertEquals(CNPJ, empresa.getCnpj());
    }
}
