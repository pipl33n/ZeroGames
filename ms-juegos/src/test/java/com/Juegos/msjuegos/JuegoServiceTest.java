package com.Juegos.msjuegos;

import com.Juegos.msjuegos.DTO.JuegoDTO;
import com.Juegos.msjuegos.Model.Juego;
import com.Juegos.msjuegos.Repository.JuegoRepository;
import com.Juegos.msjuegos.Service.JuegoService;
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
class JuegoServiceTest {

    // Repositorio FALSO — no toca la base de datos real
    @Mock
    private JuegoRepository repository;

    // Service REAL con el repositorio falso inyectado
    @InjectMocks
    private JuegoService service;

    // Datos de prueba reutilizados en todos los tests
    private Juego juego;
    private JuegoDTO juegoDTO;

    // Se ejecuta ANTES de cada test — prepara los datos base
    @BeforeEach
    void setUp() {
        juego = new Juego();
        juego.setId(1L);
        juego.setNombre("FIFA 26");
        juego.setPrecio(29.99);
        juego.setGenero("Deportes");
        juego.setPlataforma("PS5");

        juegoDTO = new JuegoDTO();
        juegoDTO.setNombre("FIFA 26");
        juegoDTO.setPrecio(29.99);
        juegoDTO.setGenero("Deportes");
        juegoDTO.setPlataforma("PS5");
    }

    // ============================================================
    // TESTS — listarJuegos
    // ============================================================

    @Test
    void listarJuegos_debeRetornarListaConJuegos() {
        // GIVEN — el repositorio tiene un juego guardado
        when(repository.findAll()).thenReturn(Arrays.asList(juego));

        // WHEN — se llama al método del servicio
        List<Juego> resultado = service.listarJuegos();

        // THEN — se verifica que la lista no es nula y contiene el juego
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("FIFA 26", resultado.get(0).getNombre());
        verify(repository, times(1)).findAll();
    }

    @Test
    void listarJuegos_cuandoNoHayJuegos_debeRetornarListaVacia() {
        // GIVEN — el repositorio está vacío
        when(repository.findAll()).thenReturn(Arrays.asList());

        // WHEN — se llama al método del servicio
        List<Juego> resultado = service.listarJuegos();

        // THEN — se verifica que la lista está vacía pero no es nula
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    // ============================================================
    // TESTS — obtenerPorId
    // ============================================================

    @Test
    void obtenerPorId_cuandoJuegoExiste_debeRetornarJuego() {
        // GIVEN — existe un juego con id 1 en el repositorio
        when(repository.findById(1L)).thenReturn(Optional.of(juego));

        // WHEN — se busca el juego con id 1
        Juego resultado = service.obtenerPorId(1L);

        // THEN — el juego retornado tiene los datos correctos
        assertNotNull(resultado);
        assertEquals("FIFA 26", resultado.getNombre());
        assertEquals(29.99, resultado.getPrecio());
        assertEquals("Deportes", resultado.getGenero());
        assertEquals("PS5", resultado.getPlataforma());
    }

    @Test
    void obtenerPorId_cuandoJuegoNoExiste_debeLanzarExcepcion() {
        // GIVEN — no existe ningún juego con id 99
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN — se verifica que lanza excepción con el mensaje correcto
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(99L);
        });
        assertEquals("Juego no encontrado con id: 99", ex.getMessage());
    }

    // ============================================================
    // TESTS — crearJuego
    // ============================================================

    @Test
    void crearJuego_cuandoNombreNoExiste_debeCrearExitosamente() {
        // GIVEN — no existe un juego con ese nombre
        when(repository.findByNombreIgnoreCase("FIFA 26")).thenReturn(Optional.empty());
        when(repository.save(any(Juego.class))).thenReturn(juego);

        // WHEN — se intenta crear el juego
        Juego resultado = service.crearJuego(juegoDTO);

        // THEN — el juego fue creado correctamente y se guardó en el repositorio
        assertNotNull(resultado);
        assertEquals("FIFA 26", resultado.getNombre());
        assertEquals(29.99, resultado.getPrecio());
        verify(repository, times(1)).save(any(Juego.class));
    }

    @Test
    void crearJuego_cuandoNombreDuplicado_debeLanzarExcepcion() {
        // GIVEN — ya existe un juego con ese nombre (regla de negocio: no duplicar nombres)
        when(repository.findByNombreIgnoreCase("FIFA 26")).thenReturn(Optional.of(juego));

        // WHEN + THEN — se verifica que lanza excepción por nombre duplicado
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.crearJuego(juegoDTO);
        });
        assertTrue(ex.getMessage().contains("Ya existe un juego con el nombre"));

        // THEN — se verifica que NUNCA se llamó a save porque falló antes
        verify(repository, never()).save(any(Juego.class));
    }

    // ============================================================
    // TESTS — actualizarJuego
    // ============================================================

    @Test
    void actualizarJuego_cuandoJuegoExiste_debeActualizarExitosamente() {
        // GIVEN — existe el juego y se prepara el DTO con nuevos datos
        JuegoDTO dtoActualizado = new JuegoDTO();
        dtoActualizado.setNombre("FIFA 27");
        dtoActualizado.setPrecio(49.99);
        dtoActualizado.setGenero("Deportes");
        dtoActualizado.setPlataforma("PS5");

        Juego juegoActualizado = new Juego();
        juegoActualizado.setId(1L);
        juegoActualizado.setNombre("FIFA 27");
        juegoActualizado.setPrecio(49.99);
        juegoActualizado.setGenero("Deportes");
        juegoActualizado.setPlataforma("PS5");

        when(repository.findById(1L)).thenReturn(Optional.of(juego));
        when(repository.save(any(Juego.class))).thenReturn(juegoActualizado);

        // WHEN — se actualiza el juego
        Juego resultado = service.actualizarJuego(1L, dtoActualizado);

        // THEN — el juego tiene los datos actualizados correctamente
        assertEquals("FIFA 27", resultado.getNombre());
        assertEquals(49.99, resultado.getPrecio());
        verify(repository, times(1)).save(any(Juego.class));
    }

    @Test
    void actualizarJuego_cuandoJuegoNoExiste_debeLanzarExcepcion() {
        // GIVEN — no existe un juego con id 99
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN — se verifica que lanza excepción con mensaje correcto
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarJuego(99L, juegoDTO);
        });
        assertEquals("Juego no encontrado con id: 99", ex.getMessage());
    }

    // ============================================================
    // TESTS — eliminarJuego
    // ============================================================

    @Test
    void eliminarJuego_cuandoJuegoExiste_debeEliminarExitosamente() {
        // GIVEN — existe un juego con id 1
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN — se elimina el juego
        service.eliminarJuego(1L);

        // THEN — se verifica que se llamó a deleteById exactamente una vez
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarJuego_cuandoJuegoNoExiste_debeLanzarExcepcion() {
        // GIVEN — no existe ningún juego con id 99
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN + THEN — se verifica que lanza excepción con mensaje correcto
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.eliminarJuego(99L);
        });
        assertEquals("Juego no encontrado con id: 99", ex.getMessage());

        // THEN — se verifica que NUNCA se llamó a deleteById
        verify(repository, never()).deleteById(anyLong());
    }

}