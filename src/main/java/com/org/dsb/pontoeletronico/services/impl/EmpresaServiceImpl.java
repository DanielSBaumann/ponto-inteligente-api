package com.org.dsb.pontoeletronico.services.impl;

import com.org.dsb.pontoeletronico.entities.Empresa;
import com.org.dsb.pontoeletronico.repositories.EmpresaRepository;
import com.org.dsb.pontoeletronico.services.EmpresaService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        log.info("Buscando empresa pelo CNPJ {}", cnpj);
        return ofNullable(empresaRepository.findByCnpj(cnpj));
    }

    @Override
    public Empresa persistir(Empresa empresa) {
        log.info("Persistindo empresa {}", empresa);
        return this.empresaRepository.save(empresa);
    }
}
