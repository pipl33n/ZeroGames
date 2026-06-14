package com.Juegos.msjuegos;

import com.Juegos.msjuegos.DTO.JuegoDTO;
import com.Juegos.msjuegos.Model.Juego;
import com.Juegos.msjuegos.Repository.JuegoRepository;
import com.Juegos.msjuegos.Service.JuegoService;
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
public class JuegoServiceTest {

    @Autowired
    private JuegoService juegoService;

    @MockitoBean
    private JuegoRepository juegoRepository;

    // Juego(Long id, String nombre, Double precio, String genero, String plataforma)

    @Test
    public void testListarJuegos() {
        when(juegoRepository.findAll()).thenReturn(List.of(new Juego(1L, "FIFA 26", 29.99, "Deportes", "PS5")));

        List<Juego> resultado = juegoService.listarJuegos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("FIFA 26", resultado.get(0).getNombre());
    }

    @Test
    public void testObtenerPorId() {
        when(juegoRepository.findById(1L)).thenReturn(Optional.of(new Juego(1L, "FIFA 26", 29.99, "Deportes", "PS5")));

        Juego resultado = juegoService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("FIFA 26", resultado.getNombre());
    }

    @Test
    public void testObtenerPorId_noExiste() {
        when(juegoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            juegoService.obtenerPorId(99L);
        });
        assertEquals("Juego no encontrado con id: 99", ex.getMessage());
    }

    @Test
    public void testCrearJuego() {
        JuegoDTO dto = new JuegoDTO();
        dto.setNombre("FIFA 26");
        dto.setPrecio(29.99);
        dto.setGenero("Deportes");
        dto.setPlataforma("PS5");

        when(juegoRepository.findByNombreIgnoreCase("FIFA 26")).thenReturn(Optional.empty());
        when(juegoRepository.save(any(Juego.class))).thenReturn(new Juego(1L, "FIFA 26", 29.99, "Deportes", "PS5"));

        Juego resultado = juegoService.crearJuego(dto);
        assertNotNull(resultado);
        assertEquals("FIFA 26", resultado.getNombre());
        assertEquals(29.99, resultado.getPrecio());
    }

    @Test
    public void testCrearJuego_nombreDuplicado() {
        JuegoDTO dto = new JuegoDTO();
        dto.setNombre("FIFA 26");
        dto.setPrecio(29.99);
        dto.setGenero("Deportes");
        dto.setPlataforma("PS5");

        when(juegoRepository.findByNombreIgnoreCase("FIFA 26")).thenReturn(Optional.of(new Juego(1L, "FIFA 26", 29.99, "Deportes", "PS5")));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            juegoService.crearJuego(dto);
        });
        assertTrue(ex.getMessage().contains("Ya existe un juego con el nombre"));
        verify(juegoRepository, never()).save(any(Juego.class));
    }

    @Test
    public void testActualizarJuego() {
        JuegoDTO dto = new JuegoDTO();
        dto.setNombre("FIFA 27");
        dto.setPrecio(49.99);
        dto.setGenero("Deportes");
        dto.setPlataforma("PS5");

        when(juegoRepository.findById(1L)).thenReturn(Optional.of(new Juego(1L, "FIFA 26", 29.99, "Deportes", "PS5")));
        when(juegoRepository.save(any(Juego.class))).thenReturn(new Juego(1L, "FIFA 27", 49.99, "Deportes", "PS5"));

        Juego resultado = juegoService.actualizarJuego(1L, dto);
        assertNotNull(resultado);
        assertEquals("FIFA 27", resultado.getNombre());
        assertEquals(49.99, resultado.getPrecio());
        verify(juegoRepository, times(1)).save(any(Juego.class));
    }

    @Test
    public void testEliminarJuego() {
        when(juegoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(juegoRepository).deleteById(1L);

        juegoService.eliminarJuego(1L);
        verify(juegoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testEliminarJuego_noExiste() {
        when(juegoRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            juegoService.eliminarJuego(99L);
        });
        assertEquals("Juego no encontrado con id: 99", ex.getMessage());
        verify(juegoRepository, never()).deleteById(anyLong());
    }
}