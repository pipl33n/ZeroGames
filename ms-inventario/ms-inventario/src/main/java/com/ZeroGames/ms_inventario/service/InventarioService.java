package com.ZeroGames.ms_inventario.service;

import com.ZeroGames.ms_inventario.model.Inventario;
import com.ZeroGames.ms_inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository repository;

    public List<Inventario> listarInventarios() {
        return repository.findAll();
    }

    public Inventario obtenerInventario(Integer id) {
        // Retorna el inventario si lo encuentra, o null si no existe (así coincide con la lógica de tu controlador)
        return repository.findById(id).orElse(null);
    }

    public Inventario crearInventario(Inventario inventario) {
        return repository.save(inventario);
    }

    public Inventario actualizarInventario(Inventario inventario) {
        // save() actúa como un "Update" si el objeto ya tiene un ID existente en la base de datos
        return repository.save(inventario);
    }

    public void eliminarInventario(Integer id) {
        repository.deleteById(id);
    }
}