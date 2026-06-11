package com.Pedidos.mspedidos.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PedidoDTO {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El id del juego es obligatorio")
    private Long juegoId;

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto debe ser mayor que 0")
    private Double montoTotal;
}
