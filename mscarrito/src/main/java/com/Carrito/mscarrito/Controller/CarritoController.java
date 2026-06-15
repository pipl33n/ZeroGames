package com.Carrito.mscarrito.Controller;

import com.Carrito.mscarrito.Model.Carrito;
import com.Carrito.mscarrito.Service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService service;

    @GetMapping
    public ResponseEntity<List<Carrito>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Carrito>> obtenerCarritoUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerCarritoUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Carrito> agregarItem(@Valid @RequestBody Carrito item) {
        return new ResponseEntity<>(service.agregarItem(item), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cantidad")
    public ResponseEntity<Carrito> actualizarCantidad(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(service.actualizarCantidad(id, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarItem(@PathVariable Long id) {
        service.eliminarItem(id);
        return ResponseEntity.ok("Item con id " + id + " eliminado del carrito correctamente");
    }

    @DeleteMapping("/usuario/{usuarioId}/vaciar")
    public ResponseEntity<String> vaciarCarrito(@PathVariable Long usuarioId) {
        service.vaciarCarrito(usuarioId);
        return ResponseEntity.ok("Carrito del usuario " + usuarioId + " vaciado correctamente");
    }
}