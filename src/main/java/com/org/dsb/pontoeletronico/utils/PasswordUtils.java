package com.org.dsb.pontoeletronico.utils;

import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.slf4j.LoggerFactory.getLogger;

public class PasswordUtils {

    private static final Logger log = getLogger(PasswordUtils.class);

    /**
     * Gera um hash utilizando o BCrypt
     *
     * @param senha
     * @return String
     */
    public static String gerarBCrypt(String senha) {
        if (senha == null) {
            return senha;
        }
        log.info("Gerar hash com o BCrypt");
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        return bCrypt.encode(senha);
    }

    /**
     * Verifica se a senha é valida
     *
     * @param senha
     * @param senhaEncoded
     * @return boolean
     */
    public static boolean senhaValida(String senha, String senhaEncoded) {
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        log.info("Verificando validade senha");
        return bCrypt.matches(senha, senhaEncoded);
    }
}
