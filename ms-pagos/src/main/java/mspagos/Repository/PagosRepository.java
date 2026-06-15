package pagos;

import java.util.ArrayList;
import java.util.List;

public class PagosRepository {
    private List<Pagos> pagos = new ArrayList<>();

    public List<Pagos> findAll() {
        return pagos;
    }

    public Pagos findById(int id) {
        return pagos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Pagos pago) {
        pagos.add(pago);
    }

    public void deleteById(int id) {
        pagos.removeIf(p -> p.getId() == id);
    }

    public void update(Pagos pago) {
        deleteById(pago.getId());
        save(pago);
    }
}
