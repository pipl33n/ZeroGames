package main.java.msinventario.Repository;

import java.util.ArrayList;
import java.util.List;


public class InventarioRepository {
    private List<Inventario> inventarios = new ArrayList<>();

    public List<Inventario> findAll() {
        return inventarios;
    }

    public Inventario findById(int id) {
        return inventarios.stream()
                .filter(inv -> inv.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Inventario inventario) {
        inventarios.add(inventario);
    }

    public void deleteById(int id) {
        inventarios.removeIf(inv -> inv.getId() == id);
    }

    public void update(Inventario inventario) {
        deleteById(inventario.getId());
        save(inventario);
    }
}