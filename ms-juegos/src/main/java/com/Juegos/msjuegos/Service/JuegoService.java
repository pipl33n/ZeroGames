package com.Juegos.msjuegos.Service;

import com.Juegos.msjuegos.DTO.JuegoDTO;
import com.Juegos.msjuegos.Model.Juego;
import com.Juegos.msjuegos.Repository.JuegoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuegoService {

    private static final Logger log = LoggerFactory.getLogger(JuegoService.class);

    @Autowired
    private JuegoRepository repository;

    public List<Juego> listarJuegos() {
        log.info("Obteniendo catálogo completo de juegos");
        return repository.findAll();
    }

    public Juego obtenerPorId(Long id) {
        log.info("Buscando juego con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado con id: " + id));
    }

    public Juego crearJuego(JuegoDTO dto) {
        log.info("Registrando nuevo juego: {}", dto.getNombre());

        // Regla de negocio: no permitir juegos con el mismo nombre
        repository.findByNombreIgnoreCase(dto.getNombre()).ifPresent(j -> {
            throw new RuntimeException("Ya existe un juego con el nombre: " + dto.getNombre());
        });

        Juego juego = new Juego();
        juego.setNombre(dto.getNombre());
        juego.setPrecio(dto.getPrecio());
        juego.setGenero(dto.getGenero());
        juego.setPlataforma(dto.getPlataforma());

        try {
            Juego guardado = repository.save(juego);
            log.info("Juego creado exitosamente con id: {}", guardado.getId());
            return guardado;
        } catch (Exception e) {
            log.error("Error al crear juego: {}", e.getMessage());
            throw new RuntimeException("Error al crear el juego: " + e.getMessage());
        }
    }

    public Juego actualizarJuego(Long id, JuegoDTO dto) {
        log.info("Actualizando juego con id: {}", id);
        Juego juego = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado con id: " + id));

        juego.setNombre(dto.getNombre());
        juego.setPrecio(dto.getPrecio());
        juego.setGenero(dto.getGenero());
        juego.setPlataforma(dto.getPlataforma());

        try {
            Juego actualizado = repository.save(juego);
            log.info("Juego actualizado exitosamente: {}", actualizado.getNombre());
            return actualizado;
        } catch (Exception e) {
            log.error("Error al actualizar juego: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar el juego: " + e.getMessage());
        }
    }

    public void eliminarJuego(Long id) {
        log.info("Eliminando juego con id: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Juego no encontrado con id: " + id);
        }
        try {
            repository.deleteById(id);
            log.info("Juego con id {} eliminado correctamente", id);
        } catch (Exception e) {
            log.error("Error al eliminar juego: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar el juego: " + e.getMessage());
        }
    }


}
