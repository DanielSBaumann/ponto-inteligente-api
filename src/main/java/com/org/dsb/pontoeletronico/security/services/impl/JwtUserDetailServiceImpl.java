package com.org.dsb.pontoeletronico.security.services.impl;

import com.org.dsb.pontoeletronico.entities.Funcionario;
import com.org.dsb.pontoeletronico.security.JwtUserFactory;
import com.org.dsb.pontoeletronico.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailServiceImpl implements UserDetailsService {

    /**
     * Alterando de Usuario para Funcionario
     */
    @Autowired
    private FuncionarioService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Funcionario> usuario = service.buscarPorEmail(username);
        if (usuario.isPresent()) {
            return JwtUserFactory.create(usuario.get());
        }
        throw new UsernameNotFoundException("Email não encontrado");
    }
}
