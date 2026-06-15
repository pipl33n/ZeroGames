package pagos;

import java.util.List;

public class PagosService {
    private PagosRepository repository = new PagosRepository();

    public List<Pagos> listarPagos() {
        return repository.findAll();
    }

    public Pagos obtenerPago(int id) {
        return repository.findById(id);
    }

    public void crearPago(Pagos pago) {
        repository.save(pago);
    }

    public void eliminarPago(int id) {
        repository.deleteById(id);
    }

    public void actualizarPago(Pagos pago) {
        repository.update(pago);
    }
}
