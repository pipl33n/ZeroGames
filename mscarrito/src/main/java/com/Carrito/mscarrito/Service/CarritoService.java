package com.Carrito.mscarrito.Service;

import com.Carrito.mscarrito.Model.Carrito;
import com.Carrito.mscarrito.Repository.CarritoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarritoService {

    private static final Logger log = LoggerFactory.getLogger(CarritoService.class);

    @Autowired
    private CarritoRepository repository;

    public List<Carrito> listarTodos() {
        log.info("Listando todos los items del carrito");
        return repository.findAll();
    }

    public List<Carrito> obtenerCarritoUsuario(Long usuarioId) {
        log.info("Obteniendo carrito del usuario con id: {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public Carrito obtenerPorId(Long id) {
        log.info("Buscando item con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con id: " + id));
    }

    public Carrito agregarItem(Carrito item) {
        log.info("Agregando juego {} al carrito del usuario {}", item.getJuegoId(), item.getUsuarioId());

        // Regla de negocio: no se puede agregar el mismo juego dos veces al carrito
        if (repository.existsByUsuarioIdAndJuegoId(item.getUsuarioId(), item.getJuegoId())) {
            throw new RuntimeException("El juego con id " + item.getJuegoId() + " ya esta en el carrito del usuario");
        }

        // Regla de negocio: cantidad maxima por item es 5
        if (item.getCantidad() > 5) {
            throw new RuntimeException("La cantidad maxima permitida por item es 5");
        }

        try {
            Carrito guardado = repository.save(item);
            log.info("Item agregado al carrito con id: {}", guardado.getId());
            return guardado;
        } catch (Exception e) {
            log.error("Error al agregar item al carrito: {}", e.getMessage());
            throw new RuntimeException("Error al agregar item: " + e.getMessage());
        }
    }

    public Carrito actualizarCantidad(Long id, Integer nuevaCantidad) {
        log.info("Actualizando cantidad del item {} a {}", id, nuevaCantidad);

        if (nuevaCantidad < 1) {
            throw new RuntimeException("La cantidad debe ser al menos 1");
        }
        if (nuevaCantidad > 5) {
            throw new RuntimeException("La cantidad maxima permitida por item es 5");
        }

        Carrito item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con id: " + id));

        item.setCantidad(nuevaCantidad);

        try {
            return repository.save(item);
        } catch (Exception e) {
            log.error("Error al actualizar cantidad: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar la cantidad: " + e.getMessage());
        }
    }

    public void eliminarItem(Long id) {
        log.info("Eliminando item con id: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Item no encontrado con id: " + id);
        }
        try {
            repository.deleteById(id);
            log.info("Item {} eliminado del carrito", id);
        } catch (Exception e) {
            log.error("Error al eliminar item: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar el item: " + e.getMessage());
        }
    }

    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        log.info("Vaciando carrito del usuario con id: {}", usuarioId);
        List<Carrito> items = repository.findByUsuarioId(usuarioId);
        if (items.isEmpty()) {
            throw new RuntimeException("El carrito del usuario " + usuarioId + " ya esta vacio");
        }
        try {
            repository.deleteByUsuarioId(usuarioId);
            log.info("Carrito del usuario {} vaciado correctamente", usuarioId);
        } catch (Exception e) {
            log.error("Error al vaciar carrito: {}", e.getMessage());
            throw new RuntimeException("Error al vaciar el carrito: " + e.getMessage());
        }
    }
}