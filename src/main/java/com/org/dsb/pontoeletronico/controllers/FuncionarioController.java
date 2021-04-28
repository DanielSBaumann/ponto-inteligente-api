package com.org.dsb.pontoeletronico.controllers;

import com.org.dsb.pontoeletronico.dtos.FuncionarioDto;
import com.org.dsb.pontoeletronico.entities.Funcionario;
import com.org.dsb.pontoeletronico.response.Response;
import com.org.dsb.pontoeletronico.services.FuncionarioService;
import com.org.dsb.pontoeletronico.services.impl.EmpresaServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.org.dsb.pontoeletronico.utils.PasswordUtils.gerarBCrypt;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);

    @Autowired
    private FuncionarioService funcionarioService;

    public FuncionarioController() {
    }

    /**
     * Atualiza os dados de um funcionario
     *
     * @param id
     * @param funcionarioDto
     * @param result
     * @return ResponseEntity<Response<FuncionarioDto>>
     * @throws NoSuchAlgorithmException
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<FuncionarioDto>> atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody FuncionarioDto funcionarioDto,
            BindingResult result)
            throws NoSuchAlgorithmException {

        log.info("Atualizando funcionario: {}:", funcionarioDto.toString());
        Response<FuncionarioDto> response = new Response<FuncionarioDto>();

        Optional<Funcionario> funcionario = this.funcionarioService
                .buscarPorId(id);
        if (!funcionario.isPresent()) {
            result
                    .addError(new ObjectError("funcionario",
                            "Funcionario nao encontrado"));
        }

        this.atualizarDadosFuncionarios(funcionario.get(),
                funcionarioDto, result);

        if (result.hasErrors()) {
            log.error("Erro validando funcionario {}",
                    result.getAllErrors());
            result.getAllErrors()
                    .forEach(error -> response
                            .getErrors()
                            .add(error.getDefaultMessage()));
            return badRequest()
                    .body(response);
        }

        this.funcionarioService
                .persistir(funcionario.get());
        response
                .setData(this.converterFuncionarioDto(funcionario.get()));

        return ok(response);
    }

    /**
     * Atualiza os dados do funcionario com base nos dados encontrados no DTO
     *
     * @param funcionario
     * @param funcionarioDto
     * @param result
     * @throws NoSuchAlgorithmException
     */
    private void atualizarDadosFuncionarios(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult result)
            throws NoSuchAlgorithmException {

        funcionario.setNome(funcionarioDto.getNome());

        if (!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
            this.funcionarioService
                    .buscarPorEmail(funcionarioDto.getEmail())
                    .ifPresent(func -> result.addError(new ObjectError("email", "Email ja cadastrado")));
            funcionario.setEmail(funcionarioDto.getEmail());
        }

        funcionario.setQtdHorasAlmoco(null);
        funcionarioDto.getQtdHorasAlmoco()
                .ifPresent(horas -> funcionario.setQtdHorasAlmoco(Float.valueOf(horas)));

        funcionario.setQtdHorasTrabalhoDia(null);
        funcionarioDto.getQtdHorasTrabalhoDia()
                .ifPresent(horas -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(horas)));

        funcionario.setValorHora(null);
        funcionarioDto.getValorHora()
                .ifPresent(horas -> funcionario.setValorHora(new BigDecimal(horas)));

        if (funcionarioDto.getSenha().isPresent()) {
            funcionario.setSenha(gerarBCrypt(funcionarioDto.getSenha().get()));
        }
    }

    /**
     * Retorna um DTO com os dados de um funcionario
     *
     * @param funcionario
     * @return FuncionarioDto
     */
    private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {
        FuncionarioDto dto = FuncionarioDto
                .builder()
                .id(funcionario.getId())
                .email(funcionario.getEmail())
                .nome(funcionario.getNome())
                .build();

        funcionario.getQtdHorasAlmocoOpt().ifPresent(hora ->
                dto.setQtdHorasAlmoco(Optional.of(Float.toString(hora))));

        funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(hora ->
                dto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(hora))));

        funcionario.getValorHoraOpt().ifPresent(hora ->
                dto.setValorHora(Optional.of(hora.toString())));

        return dto;
    }
}



























