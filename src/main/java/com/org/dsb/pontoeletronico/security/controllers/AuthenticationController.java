package com.org.dsb.pontoeletronico.security.controllers;

import com.org.dsb.pontoeletronico.response.Response;
import com.org.dsb.pontoeletronico.security.dtos.JwtAuthenticationDto;
import com.org.dsb.pontoeletronico.security.dtos.TokenDto;
import com.org.dsb.pontoeletronico.security.utils.JwtTokenUtil;
import com.org.dsb.pontoeletronico.services.impl.EmpresaServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private static final Logger log = getLogger(EmpresaServiceImpl.class);
    private static final String TOKEN_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Gera e retorna um novo token JWT
     *
     * @param authenticationDto
     * @param result
     * @return ResponseEntity<Response < TokenDto>>
     */
    @PostMapping
    public ResponseEntity<Response<TokenDto>> gerarTokenJwt(
            @Valid @RequestBody JwtAuthenticationDto authenticationDto,
            BindingResult result) {

        Response<TokenDto> response = new Response<TokenDto>();

        if (result.hasErrors()) {
            log.error("Erro validacao {}", result.getAllErrors());
            result
                    .getAllErrors()
                    .forEach(error -> response.getErrors()
                            .add(error.getDefaultMessage()));
            return badRequest()
                    .body(response);
        }

        log.info("Gerando token para o email {} ", authenticationDto.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDto.getEmail(),
                        authenticationDto.getSenha()));

        getContext()
                .setAuthentication(authentication);

        UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationDto.getEmail());

        String token = jwtTokenUtil
                .obterToken(userDetails);

        response
                .setData(new TokenDto(token));

        return ok(response);
    }

    /**
     * Gera um novo token com nova data de expiracao
     *
     * @param request
     * @return ResponseEntity<Response < TokenDto>>
     */
    @PostMapping(value = "/refresh")
    public ResponseEntity<Response<TokenDto>> refreshTokenJwt(HttpServletRequest request) {

        Response<TokenDto> response = new Response<TokenDto>();
        Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));

        if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
            token = Optional.of(token.get().substring(7));
        }

        if (!token.isPresent()) {
            response.getErrors().add("Token não informado");
        } else if (!jwtTokenUtil.tokenValido(token.get())) {
            response.getErrors().add("Token invalido ou expirado");
        }

        String refreshedToken = jwtTokenUtil
                .refreshToken(token.get());

        response
                .setData(new TokenDto(refreshedToken));

        return ok(response);
    }
}

















