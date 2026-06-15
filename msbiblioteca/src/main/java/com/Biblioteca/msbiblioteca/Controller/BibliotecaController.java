package com.Biblioteca.msbiblioteca.Controller;

import com.Biblioteca.msbiblioteca.Model.Biblioteca;
import com.Biblioteca.msbiblioteca.Service.BibliotecaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {

    @Autowired
    private BibliotecaService service;

    @GetMapping
    public ResponseEntity<List<Biblioteca>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Biblioteca>> obtenerBibliotecaUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerBibliotecaUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Biblioteca> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Biblioteca> agregarJuego(@Valid @RequestBody Biblioteca entrada) {
        return new ResponseEntity<>(service.agregarJuego(entrada), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/horas")
    public ResponseEntity<Biblioteca> registrarHoras(@PathVariable Long id, @RequestParam Integer horas) {
        return ResponseEntity.ok(service.registrarHoras(id, horas));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Biblioteca> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(service.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDeBiblioteca(@PathVariable Long id) {
        service.eliminarDeBiblioteca(id);
        return ResponseEntity.ok("Entrada con id " + id + " eliminada de la biblioteca correctamente");
    }
}
