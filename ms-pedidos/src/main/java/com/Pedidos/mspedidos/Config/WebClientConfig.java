package com.Pedidos.mspedidos.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ms.juegos.url}")
    private String msJuegosUrl;

    @Value("${ms.usuarios.url}")
    private String msUsuariosUrl;

    @Bean
    public WebClient webClientJuegos() {
        return WebClient.builder()
                .baseUrl(msJuegosUrl)
                .build();
    }

    @Bean
    public WebClient webClientUsuarios() {
        return WebClient.builder()
                .baseUrl(msUsuariosUrl)
                .build();
    }
}
