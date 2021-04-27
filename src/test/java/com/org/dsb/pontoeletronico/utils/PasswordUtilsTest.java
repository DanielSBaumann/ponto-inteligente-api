package com.org.dsb.pontoeletronico.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.org.dsb.pontoeletronico.utils.PasswordUtils.gerarBCrypt;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordUtilsTest {

    private static final String SENHA = "mypassword123";
    private final BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

    @Test
    public void testSenhaNula() throws Exception {
        assertNull(gerarBCrypt(null));
    }

    @Test
    public void gerarHashSenha() throws Exception {
        String hash = gerarBCrypt(SENHA);
        assertTrue(bCrypt.matches(SENHA, hash));
    }
}
