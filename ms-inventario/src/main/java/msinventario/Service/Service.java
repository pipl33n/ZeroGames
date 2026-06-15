package main.java.msinventario.Service;

import java.util.List;

public class InventarioService {
    private InventarioRepository repository = new InventarioRepository();

    public List<Inventario> listarInventarios() {
        return repository.findAll();
    }

    public Inventario obtenerInventario(int id) {
        return repository.findById(id);
    }

    public void crearInventario(Inventario inventario) {
        repository.save(inventario);
    }

    public void eliminarInventario(int id) {
        repository.deleteById(id);
    }

    public void actualizarInventario(Inventario inventario) {
        repository.update(inventario);
    }
}
