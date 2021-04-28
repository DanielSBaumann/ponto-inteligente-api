package com.org.dsb.pontoeletronico.controllers;

import com.org.dsb.pontoeletronico.dtos.CadastroPJDto;
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
import java.security.NoSuchAlgorithmException;

import static com.org.dsb.pontoeletronico.enums.PerfilEnum.ROLE_ADMIN;
import static com.org.dsb.pontoeletronico.utils.PasswordUtils.gerarBCrypt;
import static java.math.BigDecimal.valueOf;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/cadastro-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EmpresaService empresaService;

    public CadastroPJController() {
    }

    /**
     * Cadastra uma pessoa juridica no sistema
     *
     * @param cadastroPJDto
     * @param result
     * @return ResponseEntity<Response < CadastroPJDto>>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping
    public ResponseEntity<Response<CadastroPJDto>> cadastrar(
            @Valid @RequestBody CadastroPJDto cadastroPJDto,
            BindingResult result)
            throws NoSuchAlgorithmException {

        log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
        Response<CadastroPJDto> response = new Response<CadastroPJDto>();

        validarDadosExistentes(cadastroPJDto, result);
        Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto);
        Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto, result, empresa);

        if (result.hasErrors()) {
            log.error("Erro ao validar cadastro PJ: {}", result.getAllErrors());
            result
                    .getAllErrors()
                    .forEach(error -> response
                            .getErrors()
                            .add(error.getDefaultMessage())
                    );
            return badRequest()
                    .body(response);
        }

        this.empresaService.persistir(empresa);
        funcionario.setEmpresa(empresa);
        this.funcionarioService.persistir(funcionario);

        response.setData(this.converterCadastroPJDto(funcionario));
        return ok(response);
    }

    /**
     * Verifica se a empresa ou funcionario ja existem na base de dados
     *
     * @param cadastroPJDto
     * @param result
     */
    private void validarDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result) {
        this.empresaService
                .buscarPorCnpj(cadastroPJDto.getCnpj())
                .ifPresent(emp -> result
                        .addError(new ObjectError("Empresa",
                                "Empresa ja existente")));
        this.funcionarioService
                .buscarPorCpf(cadastroPJDto.getCpf())
                .ifPresent(func -> result
                        .addError(new ObjectError("Funcionario",
                                "Funcionario ja cadastrado")));
        this.funcionarioService
                .buscarPorEmail(cadastroPJDto.getEmail())
                .ifPresent(func -> result
                        .addError(new ObjectError("Email",
                                "Email ja cadastrado")));
    }

    /**
     * Converte os dados do DTO para empresa
     *
     * @param cadastroPJDto
     * @return Empres
     */
    private Empresa converterDtoParaEmpresa(CadastroPJDto cadastroPJDto) {
        return Empresa
                .builder()
                .cnpj(cadastroPJDto.getCnpj())
                .razaoSocial(cadastroPJDto.getRazaoSocial())
                .build();
    }

    /**
     * Converter os dados do DTO para funcionario
     *
     * @param cadastroPJDto
     * @param result
     * @param empresa
     * @return Funcionario
     * @throws NoSuchAlgorithmException
     */
    private Funcionario converterDtoParaFuncionario(CadastroPJDto cadastroPJDto, BindingResult result, Empresa empresa)
            throws NoSuchAlgorithmException {
        /**
         * Vem daqui a persistencia!!!!
         * Verificar metodo...
         */
        return Funcionario
                .builder()
                .nome(cadastroPJDto.getNome())
                .perfil(ROLE_ADMIN)
                .senha(gerarBCrypt(cadastroPJDto.getSenha()))
                .cpf(cadastroPJDto.getCpf())
                .email(cadastroPJDto.getEmail())
                .qtdHorasAlmoco(1f)
                .qtdHorasTrabalhoDia(8f)
                .valorHora(valueOf(15))
                .build();
    }

    /**
     * Popula o DTO de cadastro com os dados do funcionario e empresa
     *
     * @param funcionario
     * @return CadastroPJDto
     */
    private CadastroPJDto converterCadastroPJDto(Funcionario funcionario) {
        return CadastroPJDto
                .builder()
                .id(funcionario.getId())
                .nome(funcionario.getNome())
                .email(funcionario.getEmail())
                .cpf(funcionario.getCpf())
                .razaoSocial(funcionario.getEmpresa().getRazaoSocial())
                .cnpj(funcionario.getEmpresa().getCnpj())
                .build();
    }
}
