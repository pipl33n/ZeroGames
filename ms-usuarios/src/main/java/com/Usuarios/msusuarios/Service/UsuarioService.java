package com.Usuarios.msusuarios.Service;

import com.Usuarios.msusuarios.DTO.UsuarioDTO;
import com.Usuarios.msusuarios.Model.Usuario;
import com.Usuarios.msusuarios.Repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listarUsuarios() {
        log.info("Listando todos los usuarios");
        return repository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public Usuario crearUsuario(UsuarioDTO dto) {
        log.info("Registrando nuevo usuario con email: {}", dto.getEmail());

        // Regla de negocio: no permitir emails duplicados
        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        // Regla de negocio: rol debe ser ADMIN o CLIENTE
        if (!dto.getRol().equalsIgnoreCase("ADMIN") && !dto.getRol().equalsIgnoreCase("CLIENTE")) {
            throw new RuntimeException("El rol debe ser ADMIN o CLIENTE");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol().toUpperCase());

        try {
            Usuario guardado = repository.save(usuario);
            log.info("Usuario creado exitosamente con id: {}", guardado.getId());
            return guardado;
        } catch (Exception e) {
            log.error("Error al crear usuario: {}", e.getMessage());
            throw new RuntimeException("Error al crear el usuario: " + e.getMessage());
        }
    }

    public Usuario actualizarUsuario(Long id, UsuarioDTO dto) {
        log.info("Actualizando usuario con id: {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // Regla de negocio: si cambia el email, verificar que no esté en uso
        if (!usuario.getEmail().equalsIgnoreCase(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está en uso por otro usuario");
        }

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol().toUpperCase());

        try {
            Usuario actualizado = repository.save(usuario);
            log.info("Usuario actualizado exitosamente: {}", actualizado.getEmail());
            return actualizado;
        } catch (Exception e) {
            log.error("Error al actualizar usuario: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        try {
            repository.deleteById(id);
            log.info("Usuario con id {} eliminado correctamente", id);
        } catch (Exception e) {
            log.error("Error al eliminar usuario: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage());
        }
    }
}
