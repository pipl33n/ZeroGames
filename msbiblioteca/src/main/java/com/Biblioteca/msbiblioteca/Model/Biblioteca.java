package com.Biblioteca.msbiblioteca.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "biblioteca",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "juego_id"}))
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id del usuario es obligatorio")
    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotNull(message = "El id del juego es obligatorio")
    @Column(name = "juego_id")
    private Long juegoId;

    @Column(name = "fecha_adquisicion")
    private LocalDateTime fechaAdquisicion;

    @Min(value = 0, message = "Las horas jugadas no pueden ser negativas")
    @Column(name = "horas_jugadas")
    private Integer horasJugadas;

    private String estado;

    @PrePersist
    public void prePersist() {
        this.fechaAdquisicion = LocalDateTime.now();
        if (this.horasJugadas == null) {
            this.horasJugadas = 0;
        }
        if (this.estado == null) {
            this.estado = "ACTIVO";
        }
    }
}
