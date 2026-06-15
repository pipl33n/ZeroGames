package main.java.msinventario.Controller;

public class InventarioController {
    private InventarioService service = new InventarioService();

    public void getAllInventarios() {
        service.listarInventarios().forEach(inv ->
                System.out.println(inv.getId() + " - " + inv.getNombre() + " (" + inv.getCantidad() + ")")
        );
    }

    public void getInventarioById(int id) {
        Inventario inv = service.obtenerInventario(id);
        if (inv != null) {
            System.out.println("Encontrado: " + inv.getNombre() + " (" + inv.getCantidad() + ")");
        } else {
            System.out.println("Inventario no encontrado");
        }
    }

    public void createInventario(int id, String nombre, int cantidad) {
        service.crearInventario(new Inventario(id, nombre, cantidad));
        System.out.println("Inventario creado");
    }

    public void deleteInventario(int id) {
        service.eliminarInventario(id);
        System.out.println("Inventario eliminado");
    }

    public void updateInventario(int id, String nombre, int cantidad) {
        service.actualizarInventario(new Inventario(id, nombre, cantidad));
        System.out.println("Inventario actualizado");
    }
}
