package com.Usuarios.msusuarios.Service;

import com.Usuarios.msusuarios.DTO.UsuarioDTO;
import com.Usuarios.msusuarios.Model.Usuario;
import com.Usuarios.msusuarios.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    // Repositorio FALSO — no toca la base de datos real
    @Mock
    private UsuarioRepository repository;

    // Service REAL con el repositorio falso inyectado
    @InjectMocks
    private UsuarioService service;

    // Datos de prueba reutilizados en todos los tests
    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    // Se ejecuta ANTES de cada test — prepara los datos base
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Felipe");
        usuario.setEmail("felipe@gmail.com");
        usuario.setPassword("123456");
        usuario.setRol("CLIENTE");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Felipe");
        usuarioDTO.setEmail("felipe@gmail.com");
        usuarioDTO.setPassword("123456");
        usuarioDTO.setRol("CLIENTE");
    }

    // ============================================================
    // TESTS — listarUsuarios
    // ============================================================

    @Test
    void listarUsuarios_debeRetornarListaConUsuarios() {
        // GIVEN — el repositorio tiene un usuario guardado
        when(repository.findAll()).thenReturn(Arrays.asList(usuario)) ;

        // WHEN — se llama al método del servicio
        List<Usuario> resultado = service.listarUsuarios();

        // THEN — la lista no es nula y contiene el usuario
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Felipe", resultado.get(0).getNombre());
        verify(repository, times(1)).findAll();
    }

    @Test
    void listarUsuarios_cuandoNoHayUsuarios_debeRetornarListaVacia() {
        // GIVEN — el repositorio está vacío
        when(repository.findAll()).thenReturn(Arrays.asList());

        // WHEN — se llama al método del servicio
        List<Usuario> resultado = service.listarUsuarios();

        // THEN — la lista está vacía pero no es nula
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    // ============================================================
    // TESTS — obtenerPorId
    // ============================================================

    @Test
    void obtenerPorId_cuandoUsuarioExiste_debeRetornarUsuario() {
        // GIVEN — existe un usuario con id 1 en el repositorio
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        // WHEN — se busca el usuario con id 1
        Usuario resultado = service.obtenerPorId(1L);

        // THEN — el usuario retornado tiene los datos correctos
        assertNotNull(resultado);
        assertEquals("Felipe", resultado.getNombre());
        assertEquals("felipe@gmail.com", resultado.getEmail());
        assertEquals("CLIENTE", resultado.getRol());
    }

    @Test
    void obtenerPorId_cuandoUsuarioNoExiste_debeLanzarExcepcion() {
        // GIVEN — no existe ningún usuario con id 99
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN — se verifica que lanza excepción con el mensaje correcto
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(99L);
        });
        assertEquals("Usuario no encontrado con id: 99", ex.getMessage());
    }

    // ============================================================
    // TESTS — crearUsuario
    // ============================================================

    @Test
    void crearUsuario_cuandoEmailNoExisteYRolValido_debeCrearExitosamente() {
        // GIVEN — el email no existe y el rol es válido (CLIENTE)
        when(repository.existsByEmail("felipe@gmail.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        // WHEN — se intenta crear el usuario
        Usuario resultado = service.crearUsuario(usuarioDTO);

        // THEN — el usuario fue creado con los datos correctos
        assertNotNull(resultado);
        assertEquals("Felipe", resultado.getNombre());
        assertEquals("CLIENTE", resultado.getRol());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_cuandoEmailDuplicado_debeLanzarExcepcion() {
        // GIVEN — ya existe un usuario con ese email (regla de negocio: email único)
        when(repository.existsByEmail("felipe@gmail.com")).thenReturn(true);

        // WHEN + THEN — se verifica que lanza excepción por email duplicado
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.crearUsuario(usuarioDTO);
        });
        assertTrue(ex.getMessage().contains("Ya existe un usuario con el email"));

        // THEN — se verifica que NUNCA se llamó a save
        verify(repository, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_cuandoRolInvalido_debeLanzarExcepcion() {
        // GIVEN — el email no existe pero el rol es inválido (regla de negocio: solo ADMIN o CLIENTE)
        usuarioDTO.setRol("SUPERUSUARIO");
        when(repository.existsByEmail("felipe@gmail.com")).thenReturn(false);

        // WHEN + THEN — se verifica que lanza excepción por rol inválido
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.crearUsuario(usuarioDTO);
        });
        assertEquals("El rol debe ser ADMIN o CLIENTE", ex.getMessage());

        // THEN — se verifica que NUNCA se llamó a save
        verify(repository, never()).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_conRolAdmin_debeCrearExitosamente() {
        // GIVEN — el email no existe y el rol es ADMIN
        usuarioDTO.setRol("ADMIN");
        usuario.setRol("ADMIN");
        when(repository.existsByEmail("felipe@gmail.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        // WHEN — se crea el usuario con rol ADMIN
        Usuario resultado = service.crearUsuario(usuarioDTO);

        // THEN — el usuario fue creado con rol ADMIN
        assertNotNull(resultado);
        assertEquals("ADMIN", resultado.getRol());
    }

    // ============================================================
    // TESTS — actualizarUsuario
    // ============================================================

    @Test
    void actualizarUsuario_cuandoExisteYMismoEmail_debeActualizarExitosamente() {
        // GIVEN — existe el usuario y el email no cambia
        UsuarioDTO dtoActualizado = new UsuarioDTO();
        dtoActualizado.setNombre("Felipe Actualizado");
        dtoActualizado.setEmail("felipe@gmail.com"); // mismo email
        dtoActualizado.setPassword("nuevaPassword");
        dtoActualizado.setRol("ADMIN");

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setNombre("Felipe Actualizado");
        usuarioActualizado.setEmail("felipe@gmail.com");
        usuarioActualizado.setPassword("nuevaPassword");
        usuarioActualizado.setRol("ADMIN");

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // WHEN — se actualiza el usuario
        Usuario resultado = service.actualizarUsuario(1L, dtoActualizado);

        // THEN — el usuario tiene los datos actualizados correctamente
        assertEquals("Felipe Actualizado", resultado.getNombre());
        assertEquals("ADMIN", resultado.getRol());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_cuandoEmailNuevoYaExiste_debeLanzarExcepcion() {
        // GIVEN — el usuario existe y quiere cambiar a un email que ya usa otro (regla de negocio)
        UsuarioDTO dtoNuevoEmail = new UsuarioDTO();
        dtoNuevoEmail.setNombre("Felipe");
        dtoNuevoEmail.setEmail("otro@gmail.com"); // email diferente
        dtoNuevoEmail.setPassword("123456");
        dtoNuevoEmail.setRol("CLIENTE");

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.existsByEmail("otro@gmail.com")).thenReturn(true); // ya existe

        // WHEN + THEN — se verifica que lanza excepción por email en uso
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarUsuario(1L, dtoNuevoEmail);
        });
        assertEquals("El email ya está en uso por otro usuario", ex.getMessage());
    }

    @Test
    void actualizarUsuario_cuandoUsuarioNoExiste_debeLanzarExcepcion() {
        // GIVEN — no existe un usuario con id 99
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN — se verifica que lanza excepción con mensaje correcto
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarUsuario(99L, usuarioDTO);
        });
        assertEquals("Usuario no encontrado con id: 99", ex.getMessage());
    }

    // ============================================================
    // TESTS — eliminarUsuario
    // ============================================================

    @Test
    void eliminarUsuario_cuandoExiste_debeEliminarExitosamente() {
        // GIVEN — existe un usuario con id 1
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN — se elimina el usuario
        service.eliminarUsuario(1L);

        // THEN — se verifica que se llamó a deleteById exactamente una vez
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarUsuario_cuandoNoExiste_debeLanzarExcepcion() {
        // GIVEN — no existe ningún usuario con id 99
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN + THEN — se verifica que lanza excepción con mensaje correcto
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.eliminarUsuario(99L);
        });
        assertEquals("Usuario no encontrado con id: 99", ex.getMessage());

        // THEN — se verifica que NUNCA se llamó a deleteById
        verify(repository, never()).deleteById(anyLong());
    }
}
