package com.org.dsb.pontoeletronico.controllers;

import com.org.dsb.pontoeletronico.dtos.CadastroPFDto;
import com.org.dsb.pontoeletronico.entities.Empresa;
import com.org.dsb.pontoeletronico.entities.Funcionario;
import com.org.dsb.pontoeletronico.response.Response;
import com.org.dsb.pontoeletronico.services.EmpresaService;
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

import static com.org.dsb.pontoeletronico.enums.PerfilEnum.ROLE_USUARIO;
import static com.org.dsb.pontoeletronico.utils.PasswordUtils.gerarBCrypt;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/cadastro-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EmpresaService empresaService;

    public CadastroPFController() {
    }

    /**
     * @param cadastroPFDto
     * @param result
     * @return ResponseEntity<Response < CadastroPFDto>>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping
    public ResponseEntity<Response<CadastroPFDto>> cadastrar(
            @Valid @RequestBody CadastroPFDto cadastroPFDto,
            BindingResult result)
            throws NoSuchAlgorithmException {

        log.info("Cadastrando PF: {}", cadastroPFDto.toString());
        Response<CadastroPFDto> response = new Response<CadastroPFDto>();

        validarDadosExistentes(cadastroPFDto, result);
        Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDto, result);

        System.out.println(funcionario.toString());

        if (result.hasErrors()) {
            log.error("Erro ao validar cadastro PF: {}", result.getAllErrors());
            result
                    .getAllErrors()
                    .forEach(error -> response
                            .getErrors()
                            .add(error.getDefaultMessage())
                    );
            return badRequest()
                    .body(response);
        }

        Optional<Empresa> empresa = this.empresaService
                .buscarPorCnpj(cadastroPFDto.getCnpj());
        empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
        this.funcionarioService.persistir(funcionario);

        response.setData(this.converterCadastroPFDto(funcionario));
        return ok(response);
    }

    /**
     * Verifica se a empresa está cadastrada e se o funcionario nao existe na base de dados
     *
     * @param cadastroPFDto
     * @param result
     */
    private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
        Optional<Empresa> empresa = this.empresaService
                .buscarPorCnpj(cadastroPFDto.getCnpj());
        if (!empresa.isPresent()) {
            result
                    .addError(new ObjectError("Empresa", "Empresa nao Cadastrada"));
        }
        this.funcionarioService
                .buscarPorCpf(cadastroPFDto.getCpf())
                .ifPresent(func -> result
                        .addError(new ObjectError("Funcionario",
                                "Funcionario ja cadastrado")));
        this.funcionarioService
                .buscarPorEmail(cadastroPFDto.getEmail())
                .ifPresent(func -> result
                        .addError(new ObjectError("Email",
                                "Email ja cadastrado")));
    }

    /**
     * Converter os dados do DTO para funcionario
     *
     * @param cadastroPFDto
     * @param result
     * @return Funcionario
     */
    private Funcionario converterDtoParaFuncionario(CadastroPFDto cadastroPFDto, BindingResult result) {

        Funcionario build = Funcionario
                .builder()
                .nome(cadastroPFDto.getNome())
                .perfil(ROLE_USUARIO)
                .senha(gerarBCrypt(cadastroPFDto.getSenha()))
                .cpf(cadastroPFDto.getCpf())
                .email(cadastroPFDto.getEmail())
                .build();

        cadastroPFDto.getQtdHorasAlmoco()
                .ifPresent(hora ->
                        build.setQtdHorasAlmoco(Float.valueOf(hora)));

        cadastroPFDto.getQtdHorasTrabalhoDia()
                .ifPresent(hora ->
                        build.setQtdHorasTrabalhoDia(Float.valueOf(hora)));

        cadastroPFDto.getValorHora()
                .ifPresent(hora ->
                        build.setValorHora(new BigDecimal(hora)));

        return build;
    }

    /**
     * Popula o DTO de cadastro com os dados do funcionario e empresa
     *
     * @param funcionario
     * @return CadastroPFDto
     */
    private CadastroPFDto converterCadastroPFDto(Funcionario funcionario) {

        CadastroPFDto cadastro = CadastroPFDto
                .builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .email(funcionario.getEmail())
                .cpf(funcionario.getCpf())
                .cnpj(funcionario.getEmpresa().getCnpj())
                .build();

        funcionario.getQtdHorasAlmocoOpt().ifPresent(hora ->
                cadastro.setQtdHorasAlmoco(Optional.of(Float.toString(hora)))
        );

        funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(hora ->
                cadastro.setQtdHorasTrabalhoDia(Optional.of(Float.toString(hora)))
        );

        funcionario.getValorHoraOpt().ifPresent(hora ->
                cadastro.setValorHora(Optional.of(hora.toString()))
        );

        return cadastro;
    }
}
















