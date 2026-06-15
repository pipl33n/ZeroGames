package com.ZeroGames.ms_inventario.controller;

import com.ZeroGames.ms_inventario.model.Inventario;
import com.ZeroGames.ms_inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService service;

    @GetMapping
    public ResponseEntity<List<Inventario>> getAllInventarios() {
        return ResponseEntity.ok(service.listarInventarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> getInventarioById(@PathVariable Integer id) {
        Inventario inv = service.obtenerInventario(id);
        if (inv == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inv);
    }

    @PostMapping
    public ResponseEntity<Inventario> createInventario(@Valid @RequestBody Inventario inventario) {
        return new ResponseEntity<>(service.crearInventario(inventario), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> updateInventario(@PathVariable Integer id, @Valid @RequestBody Inventario inventario) {
        inventario.setId(id);
        return ResponseEntity.ok(service.actualizarInventario(inventario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventario(@PathVariable Integer id) {
        service.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }
}