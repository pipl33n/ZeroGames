package com.Juegos.msjuegos.Repository;

import com.Juegos.msjuegos.Model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {

    Optional<Juego> findByNombreIgnoreCase(String nombre);

}
