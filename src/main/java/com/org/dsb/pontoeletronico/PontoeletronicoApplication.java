package com.org.dsb.pontoeletronico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PontoeletronicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PontoeletronicoApplication.class, args);
    }

}
