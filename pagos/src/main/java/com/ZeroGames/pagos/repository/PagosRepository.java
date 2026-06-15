package com.ZeroGames.pagos.repository;


import com.ZeroGames.pagos.model.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagosRepository extends JpaRepository<Pagos, Integer> {
    // Listo, hereda todos los métodos de JPA
}