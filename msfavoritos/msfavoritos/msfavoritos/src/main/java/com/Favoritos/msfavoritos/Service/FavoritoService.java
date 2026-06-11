package com.Favoritos.msfavoritos.Service;
import com.Favoritos.msfavoritos.Model.Favorito;
import com.Favoritos.msfavoritos.Repository.FavoritoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;


@Service
public class FavoritoService {
    private static final Logger log = LoggerFactory.getLogger(FavoritoService.class);


    @Autowired
    private FavoritoRepository repository;


    public List<Favorito> listarFavoritos() {
        log.info("Obteniendo listado de favoritos");
        return repository.findAll();
    }


    public Favorito obtenerFavorito(Long id) {
        log.info("Buscando favorito con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Favorito no encontrado con id: " + id));
    }


    public List<Favorito> obtenerFavoritosPorUsuario(Long usuarioId) {
        log.info("Buscando favoritos del usuario con id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }


    public List<Favorito> obtenerFavoritosPorJuego(Long juegoId) {
        log.info("Buscando favoritos del juego con id: {}", juegoId);
        return repository.findByJuegoId(juegoId);
    }


    public Favorito crearFavorito(Favorito favorito) {
        try {
            log.info("Registrando nuevo favorito para usuario con id: {}", favorito.getUsuarioId());

            if (repository.existsByUsuarioIdAndJuegoId(favorito.getUsuarioId(), favorito.getJuegoId())) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST, "El usuario ya tiene este juego en favoritos"
                );
            }
            return repository.save(favorito);
        } catch (org.springframework.web.server.ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al crear favorito: {}", e.getMessage());
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear el favorito: " + e.getMessage()
            );
        }
    }

    public void eliminarFavorito(Long id) {
        try {
            log.info("Eliminando favorito con id: {}", id);

            if (!repository.existsById(id)) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Favorito no encontrado con id: " + id
                );
            }
            repository.deleteById(id);
        } catch (org.springframework.web.server.ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar favorito: {}", e.getMessage());
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el favorito: " + e.getMessage()
            );
        }

    }
}
