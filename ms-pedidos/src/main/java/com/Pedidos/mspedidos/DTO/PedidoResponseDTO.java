package com.Pedidos.mspedidos.DTO;

import lombok.Data;

@Data
public class PedidoResponseDTO {

    private Long id;
    private String estado;
    private Double montoTotal;

    // Datos del usuario obtenidos desde ms-usuarios
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;

    // Datos del juego obtenidos desde ms-juegos
    private Long juegoId;
    private String juegoNombre;
    private String juegoGenero;
    private Double juegoPrecio;
}
