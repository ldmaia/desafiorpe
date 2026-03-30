package br.com.rpe.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI productServiceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Product Service API")
                .description("Gestao do catalogo de produtos de cartao")
                .version("v1")
                .contact(new Contact().name("RPE Challenge")));
    }
}
