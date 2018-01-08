package br.com.lasa.notificacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/usuarios/*"))
                .paths(PathSelectors.ant("/api/tipo-notificacoes/*"))
                .paths(PathSelectors.ant("/api/canais/*"))
                .paths(PathSelectors.ant("/api/notificacoes/*"))
                .paths(PathSelectors.ant("/api/lojas/*"))
                .build();
                //.apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Envio de notificacao",
                "Sistema de envio de notificacao para LAIS",
                "1.0",
                "",
                new Contact("Bruno Rodrigues", "www.example.com", "myeaddress@company.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }
}
