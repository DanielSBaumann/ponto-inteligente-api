package com.org.dsb.pontoeletronico.controllers;

import com.org.dsb.pontoeletronico.dtos.EmpresaDto;
import com.org.dsb.pontoeletronico.entities.Empresa;
import com.org.dsb.pontoeletronico.response.Response;
import com.org.dsb.pontoeletronico.services.EmpresaService;
import com.org.dsb.pontoeletronico.services.impl.EmpresaServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);

    @Autowired
    private EmpresaService empresaService;

    public EmpresaController() {
    }

    /**
     * Retorna uma empresa dado um cnpj
     *
     * @param cnpj
     * @return ResponseEntity<Response < EmpresaDto>>
     */
    @GetMapping(value = "/cnpj/{cnpj}")
    public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(
            @PathVariable("cnpj") String cnpj) {

        log.info("Buscando empresa por CNPJ: {}:", cnpj);
        Response<EmpresaDto> response = new Response<EmpresaDto>();
        Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);

        if (!empresa.isPresent()) {
            log
                    .info("Empresa não encontrada para o CNPJ: {}", cnpj);
            response
                    .getErrors()
                    .add("Empresa não encontrada para o CNPJ " + cnpj);
            return badRequest()
                    .body(response);
        }
        response
                .setData(this.converterEmpresaDto(empresa.get()));
        return ok(response);
    }

    /**
     * Popula um DTO com os dados de uma empresa
     *
     * @param empresa
     * @return EmpresaDto
     */
    private EmpresaDto converterEmpresaDto(Empresa empresa) {
        return EmpresaDto
                .builder()
                .id(empresa.getId())
                .razaoSocial(empresa.getRazaoSocial())
                .cnpj(empresa.getCnpj())
                .build();
    }

}

























