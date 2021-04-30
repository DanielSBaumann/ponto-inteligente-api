package com.org.dsb.pontoeletronico.controllers;

import com.org.dsb.pontoeletronico.dtos.LancamentoDto;
import com.org.dsb.pontoeletronico.entities.Funcionario;
import com.org.dsb.pontoeletronico.entities.Lancamento;
import com.org.dsb.pontoeletronico.enums.TipoEnum;
import com.org.dsb.pontoeletronico.response.Response;
import com.org.dsb.pontoeletronico.services.FuncionarioService;
import com.org.dsb.pontoeletronico.services.LancamentoService;
import com.org.dsb.pontoeletronico.services.impl.EmpresaServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.apache.commons.lang3.EnumUtils.isValidEnum;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    public LancamentoController() {
    }

    /**
     * Retorna a listagem de lancamentos de um funcionario.
     *
     * @param funcionarioId
     * @param pag
     * @param ord
     * @param dir
     * @return ResponseEntity<Response < Page < LancamentoDto>>>
     */
    @GetMapping(value = "/funcionario/{funcionarioId}")
    public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId
    (@PathVariable("funcionarioId") Long funcionarioId,
     @RequestParam(value = "pag", defaultValue = "0") int pag,
     @RequestParam(value = "ord", defaultValue = "id") String ord,
     @RequestParam(value = "dir", defaultValue = "DESC") String dir) {

        log.info("Buscando lancamento por ID do funcionario: {}, pagina: {}", funcionarioId, pag);
        Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();

        PageRequest pageRequest = PageRequest
                .of(pag, this.qtdPorPagina, Sort.Direction.valueOf(dir), ord);
        Page<Lancamento> lancamentos = this.lancamentoService
                .buscarPorFuncionarioId(funcionarioId, pageRequest);
        Page<LancamentoDto> lancamentosDto = lancamentos
                .map(lancamento -> this.converterLancamentoDto(lancamento));

        response.setData(lancamentosDto);
        return ok(response);
    }

    /**
     * Retorna um lancamento por ID
     *
     * @param id
     * @return ResponseEntity<Response < LancamentoDto>>
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {
        log.info("Buscando lancamento por ID: {}", id);

        Response<LancamentoDto> response = new Response<LancamentoDto>();
        Optional<Lancamento> lancamento = this.lancamentoService
                .buscarPorId(id);

        if (!lancamento.isPresent()) {
            log
                    .info("Lancamento nao encontrado para o ID: {}", id);
            response
                    .getErrors()
                    .add("Lancamento nao encontrado para o id " + id);
            return badRequest()
                    .body(response);
        }

        response
                .setData(this.converterLancamentoDto(lancamento.get()));
        return ok(response);
    }

    /**
     * Adciona um novo lancamento
     *
     * @param lancamentoDto
     * @param result
     * @return ResponseEntity<Response < LancamentoDto>>
     * @throws ParseException
     */
    @PostMapping
    public ResponseEntity<Response<LancamentoDto>> adicionar(
            @Valid @RequestBody LancamentoDto lancamentoDto,
            BindingResult result) throws ParseException {

        log.info("Adcionando lancamento: {}", lancamentoDto.toString());
        Response<LancamentoDto> response = new Response<LancamentoDto>();
        validarFuncionario(lancamentoDto, result);
        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if (result.hasErrors()) {
            log.error("Erro validando lancamento{}", result.getAllErrors());
            result.getAllErrors()
                    .forEach(error -> response
                            .getErrors()
                            .add(error.getDefaultMessage()));
            return badRequest()
                    .body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return ok(response);
    }

    /**
     * Atualiza os dados de um lancamento
     *
     * @param id
     * @param lancamentoDto
     * @param result
     * @return
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody LancamentoDto lancamentoDto,
            BindingResult result)
            throws ParseException {

        log.info("Adcionando lancamento: {}", lancamentoDto.toString());
        Response<LancamentoDto> response = new Response<LancamentoDto>();
        validarFuncionario(lancamentoDto, result);
        lancamentoDto.setId(Optional.of(id));
        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if (result.hasErrors()) {
            log.error("Erro validando lancamento{}", result.getAllErrors());
            result.getAllErrors()
                    .forEach(error -> response
                            .getErrors()
                            .add(error.getDefaultMessage()));
            return badRequest()
                    .body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return ok(response);
    }

    /**
     * Remove um lancamento por ID
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {

        log.info("Removendo lancamento: {}", id);
        Response<String> response = new Response<String>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if (!lancamento.isPresent()) {
            log
                    .info("Erro ao remover devido ao lancamento ID: {} ser invalido", id);
            response
                    .getErrors()
                    .add("Erro ao remover lancamento.Registro nao encontrado para o id " + id);
            return badRequest()
                    .body(response);
        }

        this.lancamentoService.remover(id);
        return ok(new Response<String>());
    }

    /**
     * Converte uma entidade lancamento para seu respectivo DTO.
     *
     * @param lancamento
     * @return LancamentoDto
     */
    private LancamentoDto converterLancamentoDto(Lancamento lancamento) {
        return LancamentoDto
                .builder()
                .id(Optional.of(lancamento.getId()))
                .data(this.dateFormat.format(lancamento.getData()))
                .tipo(lancamento.getTipo().toString())
                .descricao(lancamento.getDescricao())
                .localizacao(lancamento.getLocalizacao())
                .funcionarioId(lancamento.getFuncionario().getId())
                .build();
    }

    /**
     * Valida um funcionario se ele é existente e valido
     * no sistema
     *
     * @param lancamentoDto
     * @param result
     */
    private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
        if (lancamentoDto.getFuncionarioId() == null) {
            result.addError(new ObjectError("funcionario", "Funcionario nao informado"));
            return;
        }
        log.info("Validando funcioraio id {}:", lancamentoDto.getFuncionarioId());
        Optional<Funcionario> funcionario = this.funcionarioService
                .buscarPorId(lancamentoDto.getFuncionarioId());
        if (!funcionario.isPresent()) {
            result.addError(new ObjectError("funcionario", "Funcionario nao encontrado. ID inexistente"));
        }
    }

    /**
     * Converte DTO para lancamento
     *
     * @param lancamentoDto
     * @param result
     * @return Lancamento
     * @throws ParseException
     */
    private Lancamento converterDtoParaLancamento(
            LancamentoDto lancamentoDto,
            BindingResult result)
            throws ParseException {

        Lancamento lancamento = new Lancamento();

        if (lancamentoDto.getId().isPresent()) {
            Optional<Lancamento> lanc = this.lancamentoService
                    .buscarPorId(lancamentoDto.getId().get());
            if (lanc.isPresent()) {
                lancamento = lanc.get();
            } else {
                result.addError(new ObjectError("lancamento", "Lancamento nao encontrado"));
            }
        } else {
            lancamento
                    .setFuncionario(new Funcionario());
            lancamento
                    .getFuncionario()
                    .setId(lancamentoDto.getFuncionarioId());
        }

        lancamento.setDescricao(lancamentoDto.getDescricao());
        lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
        lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));

        if (isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
            lancamento
                    .setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
        } else {
            result
                    .addError(new ObjectError("tipo", "Tipo invalido"));
        }

        return lancamento;
    }
}



























