package com.Pedidos.mspedidos.Controller;

import com.Pedidos.mspedidos.DTO.PedidoDTO;
import com.Pedidos.mspedidos.DTO.PedidoResponseDTO;
import com.Pedidos.mspedidos.Model.Pedido;
import com.Pedidos.mspedidos.Service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operaciones de gestión de pedidos. Se comunica con ms-juegos y ms-usuarios.")
public class PedidoController {

    @Autowired
    private PedidoService service;

    // GET /api/pedidos
    @GetMapping
    @Operation(summary = "Listar todos los pedidos", description = "Retorna la lista completa de pedidos registrados")
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(service.listarPedidos());
    }

    // GET /api/pedidos/{id}  — respuesta enriquecida con datos remotos
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPedido(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST /api/pedidos
    @PostMapping
    @Operation(summary = "Crear nuevo pedido",
            description = "Crea un pedido validando que el usuario existe en ms-usuarios y el juego existe en ms-juegos. " +
                    "El monto no puede ser menor al precio del juego.")
    public ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody PedidoDTO dto) {
        return new ResponseEntity<>(service.crearPedido(dto), HttpStatus.CREATED);
    }

    // PUT /api/pedidos/{id}/estado
    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido",
            description = "Cambia el estado de un pedido. Estados válidos: PENDIENTE, COMPLETADO, CANCELADO. " +
                    "Un pedido COMPLETADO no puede ser modificado.")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }

    // DELETE /api/pedidos/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido",
            description = "Elimina un pedido según su ID")
    public ResponseEntity<String> eliminarPedido(@PathVariable Long id) {
        service.eliminarPedido(id);
        return ResponseEntity.ok("Pedido con id " + id + " eliminado correctamente");
    }
}
