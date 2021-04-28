package com.org.dsb.pontoeletronico.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LancamentoDto {

    private Optional<Long> id = Optional.empty();
    private String tipo;
    private String descricao;
    private String localizacao;
    private Long funcionarioId;

    @NotEmpty(message = "Data não pode ser vazio!")
    private String data;

}
