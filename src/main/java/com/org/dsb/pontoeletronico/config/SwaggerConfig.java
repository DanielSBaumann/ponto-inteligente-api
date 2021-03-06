package com.org.dsb.pontoeletronico.config;

import com.org.dsb.pontoeletronico.security.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@Profile("dev")
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private final String DIR = "com.org.dsb.pontoeletronico.controllers";

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .apiInfo(apiInfo())

                .select()
                .apis(basePackage(DIR))
                .paths(any())
                .build()
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Ponto Inteligente Api")
                .description("Documentação da API de acesso aos endpoints do Ponto Inteligente")
                .version("1.0")
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        String token;
        try {
            UserDetails userDetails = this.userDetailsService
                    .loadUserByUsername("admin@mail.com");
            token = this.jwtTokenUtil
                    .obterToken(userDetails);
        } catch (Exception e) {
            token = "";
        }
        return new SecurityConfiguration(
                null, null, null, null,
                "Bearer " + token,
                ApiKeyVehicle.HEADER,
                "Authorization", ","
        );
    }

}
//                .securityContexts(Arrays.asList(securityContext()))
//                .securitySchemes(Arrays.asList(apiKey()))

//    private ApiKey apiKey() {
//        return new ApiKey("JWT", "Authorization", "header");
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder().securityReferences(defaultAuth()).build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));


//}
