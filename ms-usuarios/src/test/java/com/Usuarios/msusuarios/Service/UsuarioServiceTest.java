package com.Usuarios.msusuarios.Service;

import com.Usuarios.msusuarios.DTO.UsuarioDTO;
import com.Usuarios.msusuarios.Model.Usuario;
import com.Usuarios.msusuarios.Repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    // Usuario(Long id, String nombre, String email, String password, String rol)

    @Test
    public void testListarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(new Usuario(1L, "Felipe", "felipe@gmail.com", "123456", "CLIENTE")));

        List<Usuario> resultado = usuarioService.listarUsuarios();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Felipe", resultado.get(0).getNombre());
    }

    @Test
    public void testObtenerPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario(1L, "Felipe", "felipe@gmail.com", "123456", "CLIENTE")));

        Usuario resultado = usuarioService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Felipe", resultado.getNombre());
    }

    @Test
    public void testObtenerPorId_noExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPorId(99L);
        });
        assertEquals("Usuario no encontrado con id: 99", ex.getMessage());
    }

    @Test
    public void testCrearUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Felipe");
        dto.setEmail("felipe@gmail.com");
        dto.setPassword("123456");
        dto.setRol("CLIENTE");

        when(usuarioRepository.existsByEmail("felipe@gmail.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario(1L, "Felipe", "felipe@gmail.com", "123456", "CLIENTE"));

        Usuario resultado = usuarioService.crearUsuario(dto);
        assertNotNull(resultado);
        assertEquals("Felipe", resultado.getNombre());
        assertEquals("CLIENTE", resultado.getRol());
    }

    @Test
    public void testCrearUsuario_emailDuplicado() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Felipe");
        dto.setEmail("felipe@gmail.com");
        dto.setPassword("123456");
        dto.setRol("CLIENTE");

        when(usuarioRepository.existsByEmail("felipe@gmail.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(dto);
        });
        assertTrue(ex.getMessage().contains("Ya existe un usuario con el email"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    public void testCrearUsuario_rolInvalido() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Felipe");
        dto.setEmail("felipe@gmail.com");
        dto.setPassword("123456");
        dto.setRol("SUPERUSUARIO");

        when(usuarioRepository.existsByEmail("felipe@gmail.com")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(dto);
        });
        assertEquals("El rol debe ser ADMIN o CLIENTE", ex.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    public void testActualizarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Felipe Actualizado");
        dto.setEmail("felipe@gmail.com");
        dto.setPassword("nuevaPassword");
        dto.setRol("ADMIN");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario(1L, "Felipe", "felipe@gmail.com", "123456", "CLIENTE")));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario(1L, "Felipe Actualizado", "felipe@gmail.com", "nuevaPassword", "ADMIN"));

        Usuario resultado = usuarioService.actualizarUsuario(1L, dto);
        assertNotNull(resultado);
        assertEquals("Felipe Actualizado", resultado.getNombre());
        assertEquals("ADMIN", resultado.getRol());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testEliminarUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminarUsuario(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testEliminarUsuario_noExiste() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            usuarioService.eliminarUsuario(99L);
        });
        assertEquals("Usuario no encontrado con id: 99", ex.getMessage());
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}