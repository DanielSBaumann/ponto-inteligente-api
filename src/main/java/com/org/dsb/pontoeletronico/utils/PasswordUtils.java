package com.org.dsb.pontoeletronico.utils;

import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.slf4j.LoggerFactory.getLogger;

public class PasswordUtils {

    private static final Logger log = getLogger(PasswordUtils.class);

    public static String gerarBCrypt(String senha) {
        if (senha == null) {
            return senha;
        }
        log.info("Gerar hash com o BCrypt");
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        return bCrypt.encode(senha);
    }
}
