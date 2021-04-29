package com.org.dsb.pontoeletronico.security.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class JwtAuthenticationDto {

    @NotEmpty(message = "Email não pode ser vazio!")
    @Email(message = "Email invalido!")
    private String email;

    @NotEmpty(message = "Senha não pode ser vazio!")
    private String senha;
}
