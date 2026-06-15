package com.ZeroGames.pagos.service;


import com.ZeroGames.pagos.model.Pagos;
import com.ZeroGames.pagos.repository.PagosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagosService {

    @Autowired
    private PagosRepository repository;

    public List<Pagos> listarPagos() {
        return repository.findAll();
    }

    public Pagos obtenerPago(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Pagos crearPago(Pagos pago) {
        return repository.save(pago);
    }

    public Pagos actualizarPago(Pagos pago) {
        return repository.save(pago);
    }

    public void eliminarPago(Integer id) {
        repository.deleteById(id);
    }
}
