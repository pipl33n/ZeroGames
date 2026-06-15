package com.Biblioteca.msbiblioteca.Service;
import com.Biblioteca.msbiblioteca.Model.Biblioteca;
import com.Biblioteca.msbiblioteca.Repository.BibliotecaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BibliotecaService {

    private static final Logger log = LoggerFactory.getLogger(BibliotecaService.class);

    @Autowired
    private BibliotecaRepository repository;

    public List<Biblioteca> listarTodos() {
        log.info("Listando toda la biblioteca");
        return repository.findAll();
    }

    public List<Biblioteca> obtenerBibliotecaUsuario(Long usuarioId) {
        log.info("Obteniendo biblioteca del usuario con id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public Biblioteca obtenerPorId(Long id) {
        log.info("Buscando entrada de biblioteca con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con id: " + id));
    }

    public Biblioteca agregarJuego(Biblioteca entrada) {
        log.info("Agregando juego {} a la biblioteca del usuario {}", entrada.getJuegoId(), entrada.getUsuarioId());

        if (repository.existsByUsuarioIdAndJuegoId(entrada.getUsuarioId(), entrada.getJuegoId())) {
            throw new RuntimeException("El usuario ya tiene el juego con id " + entrada.getJuegoId() + " en su biblioteca");
        }

        try {
            Biblioteca guardado = repository.save(entrada);
            log.info("Juego agregado a la biblioteca con id: {}", guardado.getId());
            return guardado;
        } catch (Exception e) {
            log.error("Error al agregar juego a biblioteca: {}", e.getMessage());
            throw new RuntimeException("Error al agregar el juego a la biblioteca: " + e.getMessage());
        }
    }

    public Biblioteca registrarHoras(Long id, Integer horas) {
        log.info("Registrando {} horas en entrada de biblioteca id: {}", horas, id);

        // Regla de negocio: no se pueden registrar horas negativas
        if (horas < 0) {
            throw new RuntimeException("Las horas jugadas no pueden ser negativas");
        }

        Biblioteca entrada = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con id: " + id));

        // Regla de negocio: solo se puede registrar horas en juegos ACTIVOS
        if ("ARCHIVADO".equals(entrada.getEstado())) {
            throw new RuntimeException("No se pueden registrar horas en un juego archivado");
        }

        entrada.setHorasJugadas(entrada.getHorasJugadas() + horas);

        try {
            return repository.save(entrada);
        } catch (Exception e) {
            log.error("Error al registrar horas: {}", e.getMessage());
            throw new RuntimeException("Error al registrar las horas: " + e.getMessage());
        }
    }

    public Biblioteca cambiarEstado(Long id, String nuevoEstado) {
        log.info("Cambiando estado de entrada {} a {}", id, nuevoEstado);

        List<String> estadosValidos = List.of("ACTIVO", "ARCHIVADO");
        if (!estadosValidos.contains(nuevoEstado.toUpperCase())) {
            throw new RuntimeException("Estado no valido. Use: ACTIVO o ARCHIVADO");
        }

        Biblioteca entrada = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con id: " + id));

        entrada.setEstado(nuevoEstado.toUpperCase());

        try {
            return repository.save(entrada);
        } catch (Exception e) {
            log.error("Error al cambiar estado: {}", e.getMessage());
            throw new RuntimeException("Error al cambiar el estado: " + e.getMessage());
        }
    }

    public void eliminarDeBiblioteca(Long id) {
        log.info("Eliminando entrada de biblioteca con id: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Entrada no encontrada con id: " + id);
        }
        try {
            repository.deleteById(id);
            log.info("Entrada {} eliminada de la biblioteca", id);
        } catch (Exception e) {
            log.error("Error al eliminar de biblioteca: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar la entrada: " + e.getMessage());
        }
    }
}