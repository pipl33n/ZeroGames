package com.ZeroGames.pagos.controller;


import com.ZeroGames.pagos.model.Pagos;
import com.ZeroGames.pagos.service.PagosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagosController {

    @Autowired
    private PagosService service;

    @GetMapping
    public ResponseEntity<List<Pagos>> getAllPagos() {
        return ResponseEntity.ok(service.listarPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagos> getPagoById(@PathVariable Integer id) {
        Pagos pago = service.obtenerPago(id);
        if (pago == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    public ResponseEntity<Pagos> createPago(@Valid @RequestBody Pagos pago) {
        return new ResponseEntity<>(service.crearPago(pago), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pagos> updatePago(@PathVariable Integer id, @Valid @RequestBody Pagos pago) {
        pago.setId(id);
        return ResponseEntity.ok(service.actualizarPago(pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable Integer id) {
        service.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}