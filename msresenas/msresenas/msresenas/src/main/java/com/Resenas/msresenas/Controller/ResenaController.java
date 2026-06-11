package com.Resenas.msresenas.Controller;
import com.Resenas.msresenas.Model.Resena;
import com.Resenas.msresenas.Service.ResenaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/resenas")
public class ResenaController {


    @Autowired
    private ResenaService service;


    @GetMapping
    public ResponseEntity<List<Resena>> getAllResenas() {
        return ResponseEntity.ok(service.listarResenas());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Resena> getResena(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerResena(id));
    }


    @GetMapping("/juego/{juegoId}")
    public ResponseEntity<List<Resena>> getResenasByJuego(@PathVariable Long juegoId) {
        return ResponseEntity.ok(service.obtenerResenasPorJuego(juegoId));
    }


    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> getResenasByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerResenasPorUsuario(usuarioId));
    }


    @PostMapping
    public ResponseEntity<Resena> createResena(@Valid @RequestBody Resena resena) {
        return new ResponseEntity<>(service.crearResena(resena), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Resena> updateResena(@PathVariable Long id,
                                               @Valid @RequestBody Resena resena) {
        return ResponseEntity.ok(service.actualizarResena(id, resena));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        service.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }
}
