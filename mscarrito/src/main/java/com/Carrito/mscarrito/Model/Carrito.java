package com.Carrito.mscarrito.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id del usuario es obligatorio")
    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotNull(message = "El id del juego es obligatorio")
    @Column(name = "juego_id")
    private Long juegoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor que 0")
    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @Column(name = "precio_total")
    private Double precioTotal;

    @PrePersist
    @PreUpdate
    public void calcularTotal() {
        if (this.cantidad != null && this.precioUnitario != null) {
            this.precioTotal = this.cantidad * this.precioUnitario;
        }
    }
}