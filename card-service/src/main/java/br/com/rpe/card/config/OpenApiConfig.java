package br.com.rpe.card.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI cardServiceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Card Service API")
                .description("Gestao de cartoes e consumo de emissao")
                .version("v1")
                .contact(new Contact().name("RPE Challenge")));
    }
}
