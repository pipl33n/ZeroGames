package com.Favoritos.msfavoritos.Repository;
import com.Favoritos.msfavoritos.Model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuarioId(Long usuarioId);
    List<Favorito> findByJuegoId(Long juegoId);
    boolean existsByUsuarioIdAndJuegoId(Long usuarioId, Long juegoId);
}

