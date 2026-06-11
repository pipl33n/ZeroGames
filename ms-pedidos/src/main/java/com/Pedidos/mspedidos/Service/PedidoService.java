package com.Pedidos.mspedidos.Service;

import com.Pedidos.mspedidos.DTO.*;
import com.Pedidos.mspedidos.Model.Pedido;
import com.Pedidos.mspedidos.Repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository repository;

    @Autowired
    @Qualifier("webClientJuegos")
    private WebClient webClientJuegos;

    @Autowired
    @Qualifier("webClientUsuarios")
    private WebClient webClientUsuarios;

    // Obtiene datos del usuario desde ms-usuarios
    private UsuarioClientDTO obtenerUsuario(Long usuarioId) {
        try {
            return webClientUsuarios.get()
                    .uri("/api/usuarios/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(UsuarioClientDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error al consultar ms-usuarios para id {}: {}", usuarioId, e.getMessage());
            throw new RuntimeException("Usuario no encontrado en ms-usuarios con id: " + usuarioId);
        }
    }

    // Obtiene datos del juego desde ms-juegos
    private JuegoClientDTO obtenerJuego(Long juegoId) {
        try {
            return webClientJuegos.get()
                    .uri("/api/juegos/{id}", juegoId)
                    .retrieve()
                    .bodyToMono(JuegoClientDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error al consultar ms-juegos para id {}: {}", juegoId, e.getMessage());
            throw new RuntimeException("Juego no encontrado en ms-juegos con id: " + juegoId);
        }
    }

    public List<Pedido> listarPedidos() {
        log.info("Listando todos los pedidos");
        return repository.findAll();
    }

    public PedidoResponseDTO obtenerPorId(Long id) {
        log.info("Buscando pedido con id: {}", id);
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        return construirResponse(pedido);
    }

    public PedidoResponseDTO crearPedido(PedidoDTO dto) {
        log.info("Creando pedido para usuarioId: {} y juegoId: {}", dto.getUsuarioId(), dto.getJuegoId());

        // Regla de negocio: verificar que el usuario existe en ms-usuarios
        UsuarioClientDTO usuario = obtenerUsuario(dto.getUsuarioId());
        log.info("Usuario validado: {}", usuario.getEmail());

        // Regla de negocio: verificar que el juego existe en ms-juegos
        JuegoClientDTO juego = obtenerJuego(dto.getJuegoId());
        log.info("Juego validado: {}", juego.getNombre());

        // Regla de negocio: el monto no puede ser menor al precio del juego
        if (dto.getMontoTotal() < juego.getPrecio()) {
            throw new RuntimeException("El monto total no puede ser menor al precio del juego: " + juego.getPrecio());
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(dto.getUsuarioId());
        pedido.setJuegoId(dto.getJuegoId());
        pedido.setMontoTotal(dto.getMontoTotal());
        pedido.setEstado("PENDIENTE");

        try {
            Pedido guardado = repository.save(pedido);
            log.info("Pedido creado exitosamente con id: {}", guardado.getId());
            return construirResponse(guardado);
        } catch (Exception e) {
            log.error("Error al guardar pedido: {}", e.getMessage());
            throw new RuntimeException("Error al crear el pedido: " + e.getMessage());
        }
    }

    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        log.info("Actualizando estado del pedido id: {} a {}", id, nuevoEstado);

        // Regla de negocio: solo estados válidos
        if (!nuevoEstado.equalsIgnoreCase("PENDIENTE")
                && !nuevoEstado.equalsIgnoreCase("COMPLETADO")
                && !nuevoEstado.equalsIgnoreCase("CANCELADO")) {
            throw new RuntimeException("Estado inválido. Use: PENDIENTE, COMPLETADO o CANCELADO");
        }

        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        // Regla de negocio: un pedido COMPLETADO no puede modificarse
        if (pedido.getEstado().equalsIgnoreCase("COMPLETADO")) {
            throw new RuntimeException("No se puede modificar un pedido ya completado");
        }

        pedido.setEstado(nuevoEstado.toUpperCase());

        try {
            Pedido actualizado = repository.save(pedido);
            log.info("Estado del pedido {} actualizado a {}", id, actualizado.getEstado());
            return actualizado;
        } catch (Exception e) {
            log.error("Error al actualizar estado: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar el estado: " + e.getMessage());
        }
    }

    public void eliminarPedido(Long id) {
        log.info("Eliminando pedido con id: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con id: " + id);
        }
        try {
            repository.deleteById(id);
            log.info("Pedido con id {} eliminado correctamente", id);
        } catch (Exception e) {
            log.error("Error al eliminar pedido: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar el pedido: " + e.getMessage());
        }
    }

    // Construye el response enriquecido con datos de los otros microservicios
    private PedidoResponseDTO construirResponse(Pedido pedido) {
        UsuarioClientDTO usuario = obtenerUsuario(pedido.getUsuarioId());
        JuegoClientDTO juego = obtenerJuego(pedido.getJuegoId());

        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setId(pedido.getId());
        response.setEstado(pedido.getEstado());
        response.setMontoTotal(pedido.getMontoTotal());
        response.setUsuarioId(usuario.getId());
        response.setUsuarioNombre(usuario.getNombre());
        response.setUsuarioEmail(usuario.getEmail());
        response.setJuegoId(juego.getId());
        response.setJuegoNombre(juego.getNombre());
        response.setJuegoGenero(juego.getGenero());
        response.setJuegoPrecio(juego.getPrecio());

        return response;
    }
}
