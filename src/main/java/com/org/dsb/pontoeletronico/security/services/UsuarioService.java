package com.org.dsb.pontoeletronico.security.services;

import com.org.dsb.pontoeletronico.security.entities.Usuario;

import java.util.Optional;

public interface UsuarioService {

    /**
     * Busca e retorna um usuario dado um email
     *
     * @param email
     * @return Optional<Usuario>
     */
    Optional<Usuario> buscarPorEmail(String email);
}
