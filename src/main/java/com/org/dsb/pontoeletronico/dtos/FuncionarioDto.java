package com.org.dsb.pontoeletronico.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioDto {

    private Long id;

    @NotEmpty(message = "Nome não pode ser vazio!")
    @Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres")
    private String nome;

    @NotEmpty(message = "Email não pode ser vazio!")
    @Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres")
    @Email(message = "Email invalido!")
    private String email;

    private Optional<String> senha = Optional.empty();
    private Optional<String> valorHora = Optional.empty();
    private Optional<String> qtdHorasTrabalhoDia = Optional.empty();
    private Optional<String> qtdHorasAlmoco = Optional.empty();
}
