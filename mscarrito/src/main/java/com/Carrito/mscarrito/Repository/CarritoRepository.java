package com.Carrito.mscarrito.Repository;

import com.Carrito.mscarrito.Model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    List<Carrito> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioIdAndJuegoId(Long usuarioId, Long juegoId);

    void deleteByUsuarioId(Long usuarioId);
}
