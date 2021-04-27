package com.org.dsb.pontoeletronico.services;

import com.org.dsb.pontoeletronico.entities.Funcionario;

import java.util.Optional;

public interface FuncionarioService {

    /**
     * Persiste um uncionario na base de dados.
     *
     * @param funcionario
     * @return Funcionario
     */
    Funcionario persistir(Funcionario funcionario);

    /**
     * Busca e retorna um funcionario dado um CPF
     *
     * @param cpf
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> buscarPorCpf(String cpf);

    /**
     * Busca e retorna um funcionario por email
     *
     * @param email
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> buscarPorEmail(String email);

    /**
     * Busca e retorna um funcionario por id
     *
     * @param id
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> buscarPorId(Long id);

}
