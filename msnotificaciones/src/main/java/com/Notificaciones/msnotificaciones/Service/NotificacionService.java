package com.Notificaciones.msnotificaciones.Service;

import com.Notificaciones.msnotificaciones.Model.Notificacion;
import com.Notificaciones.msnotificaciones.Repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    @Autowired
    private NotificacionRepository repository;

    public List<Notificacion> listarTodas() {
        log.info("Listando todas las notificaciones");
        return repository.findAll();
    }

    public Notificacion obtenerPorId(Long id) {
        log.info("Buscando notificacion con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada con id: " + id));
    }

    public List<Notificacion> obtenerPorUsuario(Long usuarioId) {
        log.info("Buscando notificaciones del usuario con id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public Notificacion crearNotificacion(Notificacion notificacion) {
        log.info("Creando notificacion de tipo {} para usuario {}", notificacion.getTipo(), notificacion.getUsuarioId());

        // Regla de negocio: el tipo debe ser valido
        List<String> tiposValidos = List.of("EMAIL", "PUSH", "SMS");
        if (!tiposValidos.contains(notificacion.getTipo().toUpperCase())) {
            throw new RuntimeException("Tipo de notificacion no valido. Use: EMAIL, PUSH o SMS");
        }

        // Regla de negocio: el mensaje debe tener al menos 5 caracteres
        if (notificacion.getMensaje().trim().length() < 5) {
            throw new RuntimeException("El mensaje debe tener al menos 5 caracteres");
        }

        notificacion.setTipo(notificacion.getTipo().toUpperCase());

        try {
            Notificacion guardada = repository.save(notificacion);
            log.info("Notificacion creada con id: {}", guardada.getId());
            return guardada;
        } catch (Exception e) {
            log.error("Error al crear notificacion: {}", e.getMessage());
            throw new RuntimeException("Error al crear la notificacion: " + e.getMessage());
        }
    }

    public Notificacion cambiarEstado(Long id, String nuevoEstado) {
        log.info("Cambiando estado de notificacion {} a {}", id, nuevoEstado);

        List<String> estadosValidos = List.of("PENDIENTE", "ENVIADA", "FALLIDA");
        if (!estadosValidos.contains(nuevoEstado.toUpperCase())) {
            throw new RuntimeException("Estado no valido. Use: PENDIENTE, ENVIADA o FALLIDA");
        }

        Notificacion notificacion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada con id: " + id));

        // Regla de negocio: una notificacion ENVIADA no puede volver a PENDIENTE
        if ("ENVIADA".equals(notificacion.getEstado()) && "PENDIENTE".equalsIgnoreCase(nuevoEstado)) {
            throw new RuntimeException("Una notificacion ya enviada no puede volver a estado PENDIENTE");
        }

        notificacion.setEstado(nuevoEstado.toUpperCase());

        try {
            return repository.save(notificacion);
        } catch (Exception e) {
            log.error("Error al cambiar estado: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar estado: " + e.getMessage());
        }
    }

    public void eliminarNotificacion(Long id) {
        log.info("Eliminando notificacion con id: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Notificacion no encontrada con id: " + id);
        }
        try {
            repository.deleteById(id);
            log.info("Notificacion {} eliminada correctamente", id);
        } catch (Exception e) {
            log.error("Error al eliminar notificacion: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar la notificacion: " + e.getMessage());
        }
    }
}