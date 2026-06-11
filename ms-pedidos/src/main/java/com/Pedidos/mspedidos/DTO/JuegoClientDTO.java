package com.Pedidos.mspedidos.DTO;

import lombok.Data;

@Data
public class JuegoClientDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private String genero;
    private String plataforma;
}
