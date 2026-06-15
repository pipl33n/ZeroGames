package com.Pagos.mspagos.Controller; // Ajusta el package según tu proyecto

import com.Pagos.mspagos.Model.Pagos;       // Ajusta la ruta de tu modelo
import com.Pagos.mspagos.Service.PagosService; // Ajusta la ruta de tu servicio
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagosController {

    @Autowired
    private PagosService service;

    // 1. Listar todos los pagos
    @GetMapping
    public ResponseEntity<List<Pagos>> getAllPagos() {
        return ResponseEntity.ok(service.listarPagos());
    }

    // 2. Obtener un pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pagos> getPagoById(@PathVariable int id) {
        Pagos pago = service.obtenerPago(id);
        if (pago == null) {
            return ResponseEntity.notFound().build();
            // Si el servicio devuelve null, respondemos con un 404 Not Found de forma elegante
        }
        return ResponseEntity.ok(pago);
    }

    // 3. Crear un nuevo pago
    // Nota: Ahora recibe un objeto 'Pagos' en el cuerpo JSON en vez de parámetros sueltos
    @PostMapping
    public ResponseEntity<Pagos> createPago(@Valid @RequestBody Pagos pago) {
        return new ResponseEntity<>(service.crearPago(pago), HttpStatus.CREATED);
    }

    // 4. Actualizar un pago existente
    @PutMapping("/{id}")
    public ResponseEntity<Pagos> updatePago(@PathVariable int id,
                                            @Valid @RequestBody Pagos pago) {
        // Asumiendo que tu servicio o modelo maneja el ID, puedes forzar que use el del Path
        pago.setId(id);
        return ResponseEntity.ok(service.actualizarPago(pago));
    }

    // 5. Eliminar un pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable int id) {
        service.eliminarPago(id);
        return ResponseEntity.noContent().build(); // Retorna un 204 No Content (Éxito sin cuerpo)
    }
}