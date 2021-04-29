package com.org.dsb.pontoeletronico.security;

import com.org.dsb.pontoeletronico.enums.*;
import com.org.dsb.pontoeletronico.security.entities.Usuario;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class JwtUserFactory {

    /**
     * Converte e gera um JwtUser com base nos dados de um funcionario
     *
     * @param usuario
     * @return JwtUser
     */
    public static JwtUser create(Usuario usuario) {
        return new JwtUser(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getSenha(),
                mapToGrantedAuthorities(usuario.getPerfil()));
    }

    /**
     * Converte o perfil do usuario para
     * o formato utilizado pelo Spring Security
     *
     * @param perfilEnum
     * @return List<GrantedAuthority>
     */
    public static List<GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfilEnum) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(perfilEnum.toString()));
        return authorities;
    }
}
