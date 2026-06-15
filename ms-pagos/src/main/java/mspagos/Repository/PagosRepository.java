package com.Pagos.mspagos.Repository; // Ajusta el package según tu proyecto

import com.Pagos.mspagos.Model.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagosRepository extends JpaRepository<Pagos, Integer> {
    // JpaRepository ya te regala los métodos: findAll(), findById(), save(), deleteById()

    // Si en el futuro necesitas buscar pagos por un cliente o estado, los agregarías aquí.
}