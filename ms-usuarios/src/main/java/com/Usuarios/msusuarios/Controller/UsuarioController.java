package com.Usuarios.msusuarios.Controller;

import com.Usuarios.msusuarios.DTO.UsuarioDTO;
import com.Usuarios.msusuarios.Model.Usuario;
import com.Usuarios.msusuarios.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // GET /api/usuarios
    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Retorna la lista completa de usuarios registrados")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(service.listarUsuarios());
    }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico según su ID")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }


    // POST /api/usuarios
    @PostMapping
    @Operation(summary = "Crear nuevo usuario",
            description = "Registra un nuevo usuario. El email debe ser único y el rol debe ser ADMIN o CLIENTE.")
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody UsuarioDTO dto) {
        return new ResponseEntity<>(service.crearUsuario(dto), HttpStatus.CREATED);
    }

    // PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente. Si cambia el email, no debe estar en uso.")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.actualizarUsuario(id, dto));
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario según su ID")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        service.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario con id " + id + " eliminado correctamente");
    }
}
