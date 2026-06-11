package com.Pedidos.mspedidos.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Solo guardamos los IDs, la info completa se obtiene via WebClient
    @NotNull(message = "El id del usuario es obligatorio")
    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotNull(message = "El id del juego es obligatorio")
    @Column(name = "juego_id")
    private Long juegoId;

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto debe ser mayor que 0")
    private Double montoTotal;

    private String estado; // PENDIENTE, COMPLETADO, CANCELADO
}
