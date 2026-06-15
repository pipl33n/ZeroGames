package com.Biblioteca.msbiblioteca.Repository;

import com.Biblioteca.msbiblioteca.Model.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BibliotecaRepository extends JpaRepository<Biblioteca, Long> {

    List<Biblioteca> findByUsuarioId(Long usuarioId);

    Optional<Biblioteca> findByUsuarioIdAndJuegoId(Long usuarioId, Long juegoId);

    boolean existsByUsuarioIdAndJuegoId(Long usuarioId, Long juegoId);
}