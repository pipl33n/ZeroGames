package com.Pedidos.mspedidos.Config;
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
                        .title("ZeroGames - Microservicio de Pedidos")
                        .version("1.0.0")
                        .description("API REST para la gestión de pedidos. " +
                                "Se comunica con ms-juegos y ms-usuarios para validar datos y generar respuestas enriquecidas.")
                        .contact(new Contact()
                                .name("Equipo ZeroGames")
                                .email("zerogames@duoc.cl")));
    }
}
