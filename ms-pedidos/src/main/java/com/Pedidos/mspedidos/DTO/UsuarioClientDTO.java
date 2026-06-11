package com.Pedidos.mspedidos.DTO;

import lombok.Data;

@Data
public class UsuarioClientDTO {
    private Long id;
    private String nombre;
    private String email;
    private String rol;
}
