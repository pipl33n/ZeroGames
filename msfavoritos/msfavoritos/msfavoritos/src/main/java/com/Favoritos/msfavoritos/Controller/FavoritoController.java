package com.Favoritos.msfavoritos.Controller;

import com.Favoritos.msfavoritos.Model.Favorito;
import com.Favoritos.msfavoritos.Service.FavoritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {
    @Autowired
    private FavoritoService service;


    @GetMapping
    public ResponseEntity<List<Favorito>> getAllFavoritos() {
        return ResponseEntity.ok(service.listarFavoritos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Favorito> getFavorito(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerFavorito(id));
    }


    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Favorito>> getFavoritosByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerFavoritosPorUsuario(usuarioId));
    }


    @GetMapping("/juego/{juegoId}")
    public ResponseEntity<List<Favorito>> getFavoritosByJuego(@PathVariable Long juegoId) {
        return ResponseEntity.ok(service.obtenerFavoritosPorJuego(juegoId));
    }


    @PostMapping
    public ResponseEntity<Favorito> createFavorito(@Valid @RequestBody Favorito favorito) {
        return new ResponseEntity<>(service.crearFavorito(favorito), HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFavorito(@PathVariable Long id) {
        service.eliminarFavorito(id);
        return ResponseEntity.noContent().build();
    }
}


