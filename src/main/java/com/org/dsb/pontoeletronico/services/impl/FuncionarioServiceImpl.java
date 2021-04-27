package com.org.dsb.pontoeletronico.services.impl;

import com.org.dsb.pontoeletronico.entities.Funcionario;
import com.org.dsb.pontoeletronico.repositories.FuncionarioRepository;
import com.org.dsb.pontoeletronico.services.FuncionarioService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public Funcionario persistir(Funcionario funcionario) {
        log.info("Persistindo funcionario: {}", funcionario);
        return this.funcionarioRepository.save(funcionario);
    }

    public Optional<Funcionario> buscarPorCpf(String cpf) {
        log.info("Buscando funcionario por CPF : {}", cpf);
        return ofNullable(this.funcionarioRepository.findByCpf(cpf));
    }

    public Optional<Funcionario> buscarPorEmail(String email) {
        log.info("Buscando funcionario pelo email : {}", email);
        return ofNullable(this.funcionarioRepository.findByEmail(email));
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        log.info("Buscando funcionario pelo id : {}", id);
        return this.funcionarioRepository.findById(id);
    }
}
