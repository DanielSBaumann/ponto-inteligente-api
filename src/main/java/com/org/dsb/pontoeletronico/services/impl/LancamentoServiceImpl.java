package com.org.dsb.pontoeletronico.services.impl;

import com.org.dsb.pontoeletronico.entities.Lancamento;
import com.org.dsb.pontoeletronico.repositories.LancamentoRepository;
import com.org.dsb.pontoeletronico.services.LancamentoService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
        log.info("Buscando um lancamento para o funcinario id: {}", funcionarioId);
        return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
    }

    public Optional<Lancamento> buscarPorId(Long id) {
        log.info("Buscando um lancamento pelo id: {}", id);
        return this.lancamentoRepository.findById(id);
    }

    public Lancamento persistir(Lancamento lancamento) {
        log.info("Persistindo o lancamento: {}", lancamento);
        return this.lancamentoRepository.save(lancamento);
    }

    public void remover(Long id) {
        log.info("Removendo lancamento pelo id: {}", id);
        this.lancamentoRepository.deleteById(id);
    }
}
