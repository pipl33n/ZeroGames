package com.Juegos.msjuegos.Controller;

import com.Juegos.msjuegos.DTO.JuegoDTO;
import com.Juegos.msjuegos.Model.Juego;
import com.Juegos.msjuegos.Service.JuegoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/juegos")
@Tag(name = "Juegos", description = "Operaciones del catálogo de videojuegos")
public class JuegoController {

    @Autowired
    private JuegoService service;

    // GET /api/juegos
    @GetMapping
    @Operation(summary = "Listar todos los juegos", description = "Retorna el catálogo completo de videojuegos disponibles")
    public ResponseEntity<List<Juego>> listarJuegos() {
        return ResponseEntity.ok(service.listarJuegos());
    }

    // GET /api/juegos/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Obtener juego por ID", description = "Retorna un juego específico según su ID")
    public ResponseEntity<Juego> obtenerJuego(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST /api/juegos
    @PostMapping
    @Operation(summary = "Crear nuevo juego", description = "Registra un nuevo videojuego en el catálogo. No se permiten nombres duplicados.")
    public ResponseEntity<Juego> crearJuego(@Valid @RequestBody JuegoDTO dto) {
        return new ResponseEntity<>(service.crearJuego(dto), HttpStatus.CREATED);
    }

    // PUT /api/juegos/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar juego", description = "Actualiza los datos de un juego existente según su ID")
    public ResponseEntity<Juego> actualizarJuego(@PathVariable Long id, @Valid @RequestBody JuegoDTO dto) {
        return ResponseEntity.ok(service.actualizarJuego(id, dto));
    }

    // DELETE /api/juegos/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar juego", description = "Elimina un juego del catálogo según su ID")
    public ResponseEntity<String> eliminarJuego(@PathVariable Long id) {
        service.eliminarJuego(id);
        return ResponseEntity.ok("Juego con id " + id + " eliminado correctamente");
    }
}
