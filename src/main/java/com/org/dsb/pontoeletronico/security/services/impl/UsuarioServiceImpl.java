package com.org.dsb.pontoeletronico.security.services.impl;

import com.org.dsb.pontoeletronico.security.entities.Usuario;
import com.org.dsb.pontoeletronico.security.repositories.UsuarioRepository;
import com.org.dsb.pontoeletronico.security.services.UsuarioService;
import com.org.dsb.pontoeletronico.services.impl.EmpresaServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        log.info("Buscando usuario pelo email {}", email);
        return this.usuarioRepository.findByEmail(email);
    }
}
