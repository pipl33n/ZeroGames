package com.Resenas.msresenas.Service;
import com.Resenas.msresenas.Model.Resena;
import com.Resenas.msresenas.Repository.ResenaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ResenaService {


    private static final Logger log = LoggerFactory.getLogger(ResenaService.class);


    @Autowired
    private ResenaRepository repository;


    public List<Resena> listarResenas() {
        log.info("Obteniendo listado de reseñas");
        return repository.findAll();
    }


    public Resena obtenerResena(Long id) {
        log.info("Buscando reseña con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reseña no encontrada con id: " + id));
    }


    public List<Resena> obtenerResenasPorJuego(Long juegoId) {
        log.info("Buscando reseñas del juego con id: {}", juegoId);
        return repository.findByJuegoId(juegoId);
    }


    public List<Resena> obtenerResenasPorUsuario(Long usuarioId) {
        log.info("Buscando reseñas del usuario con id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }


    public Resena crearResena(Resena resena) {
        try {
            log.info("Registrando nueva reseña para juego con id: {}", resena.getJuegoId());
            return repository.save(resena);
        } catch (Exception e) {
            log.error("Error al crear reseña: {}", e.getMessage());
            throw new RuntimeException("Error al crear la reseña: " + e.getMessage());
        }
    }


    public Resena actualizarResena(Long id, Resena resenaActualizada) {
        try {
            log.info("Actualizando reseña con id: {}", id);
            Resena resenaExistente = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Reseña no encontrada con id: " + id));


            resenaExistente.setJuegoId(resenaActualizada.getJuegoId());
            resenaExistente.setUsuarioId(resenaActualizada.getUsuarioId());
            resenaExistente.setComentario(resenaActualizada.getComentario());
            resenaExistente.setPuntuacion(resenaActualizada.getPuntuacion());


            return repository.save(resenaExistente);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar reseña: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    public void eliminarResena(Long id) {
        try {
            log.info("Eliminando reseña con id: {}", id);
            if (!repository.existsById(id)) {
                throw new NoSuchElementException("Reseña no encontrada con id: " + id);
            }
            repository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar reseña: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar la reseña: " + e.getMessage());
        }
    }
}

