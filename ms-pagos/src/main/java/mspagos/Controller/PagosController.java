package pagos;

public class PagosController {
    private PagosService service = new PagosService();

    public void getAllPagos() {
        service.listarPagos().forEach(p ->
                System.out.println(p.getId() + " - " + p.getDescripcion() + " ($" + p.getMonto() + ")")
        );
    }

    public void getPagoById(int id) {
        Pagos p = service.obtenerPago(id);
        if (p != null) {
            System.out.println("Encontrado: " + p.getDescripcion() + " ($" + p.getMonto() + ")");
        } else {
            System.out.println("Pago no encontrado");
        }
    }

    public void createPago(int id, String descripcion, double monto) {
        service.crearPago(new Pagos(id, descripcion, monto));
        System.out.println("Pago creado");
    }

    public void deletePago(int id) {
        service.eliminarPago(id);
        System.out.println("Pago eliminado");
    }

    public void updatePago(int id, String descripcion, double monto) {
        service.actualizarPago(new Pagos(id, descripcion, monto));
        System.out.println("Pago actualizado");
    }
}
