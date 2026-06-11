package com.Usuarios.msusuarios.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ZeroGames - Microservicio de Usuarios")
                        .version("1.0.0")
                        .description("API REST para la gestión de usuarios. " +
                                "Permite registrar, listar, actualizar y eliminar usuarios con roles ADMIN y CLIENTE.")
                        .contact(new Contact()
                                .name("Equipo ZeroGames")
                                .email("zerogames@duoc.cl")));
    }
}