package com.org.dsb.pontoeletronico.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastroPJDto {

    private Long id;

    @NotEmpty(message = "CNome não pode ser vazio!")
    @Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres")
    private String nome;

    @NotEmpty(message = "Email não pode ser vazio!")
    @Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres")
    @Email(message = "Email invalido!")
    private String email;

    @NotEmpty(message = "Senha não pode ser vazia!")
    @Length(min = 3, max = 20, message = "Senha deve conter entre 6 e 20 caracteres")
    private String senha;

    @NotEmpty(message = "Cpf não pode ser vazio!")
    @CPF(message = "Cpf invalido!")
    private String cpf;

    @NotEmpty(message = "Razão social não pode ser vazio!")
    @Length(min = 5, max = 200, message = "Razão social deve conter entre 5 e 200 caracteres")
    private String razaoSocial;

    @NotEmpty(message = "Cnpj não pode ser vazio!")
    @CNPJ(message = "Cnpj invalido!")
    private String cnpj;
}
