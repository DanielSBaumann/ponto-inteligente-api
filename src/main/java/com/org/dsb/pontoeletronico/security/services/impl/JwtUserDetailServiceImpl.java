package com.org.dsb.pontoeletronico.security.services.impl;

import com.org.dsb.pontoeletronico.security.JwtUserFactory;
import com.org.dsb.pontoeletronico.security.entities.Usuario;
import com.org.dsb.pontoeletronico.security.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(username);
        if (usuario.isPresent()) {
            return JwtUserFactory.create(usuario.get());
        }
        throw new UsernameNotFoundException("Email não encontrado");
    }
}
