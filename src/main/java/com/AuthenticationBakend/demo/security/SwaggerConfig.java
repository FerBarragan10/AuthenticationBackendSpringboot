package com.AuthenticationBakend.demo.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Mi API")
                        .description("Documentación de mi API Spring Boot")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
