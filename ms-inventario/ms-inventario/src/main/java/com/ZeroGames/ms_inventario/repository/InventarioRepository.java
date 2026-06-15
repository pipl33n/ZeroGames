package com.ZeroGames.ms_inventario.repository;

import com.ZeroGames.ms_inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    // JpaRepository ya incluye por defecto: findAll(), findById(), save(), deleteById(), etc.
}