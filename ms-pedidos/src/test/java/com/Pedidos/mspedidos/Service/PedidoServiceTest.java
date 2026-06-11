package com.Pedidos.mspedidos.Service;

import com.Pedidos.mspedidos.DTO.JuegoClientDTO;
import com.Pedidos.mspedidos.DTO.PedidoDTO;
import com.Pedidos.mspedidos.DTO.PedidoResponseDTO;
import com.Pedidos.mspedidos.DTO.UsuarioClientDTO;
import com.Pedidos.mspedidos.Model.Pedido;
import com.Pedidos.mspedidos.Repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {
    // Repositorio FALSO — no toca la base de datos real
    @Mock
    private PedidoRepository repository;

    // WebClients FALSOS — simulan llamadas HTTP a ms-juegos y ms-usuarios
    @Mock
    private WebClient webClientJuegos;

    @Mock
    private WebClient webClientUsuarios;

    // Mocks internos para encadenar las llamadas del WebClient
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    // Service REAL con todo lo anterior inyectado
    @InjectMocks
    private PedidoService service;

    // Datos de prueba reutilizados en todos los tests
    private Pedido pedido;
    private PedidoDTO pedidoDTO;
    private UsuarioClientDTO usuarioClientDTO;
    private JuegoClientDTO juegoClientDTO;

    // Se ejecuta ANTES de cada test — prepara los datos base
    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setUsuarioId(1L);
        pedido.setJuegoId(1L);
        pedido.setMontoTotal(29.99);
        pedido.setEstado("PENDIENTE");

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setUsuarioId(1L);
        pedidoDTO.setJuegoId(1L);
        pedidoDTO.setMontoTotal(29.99);

        usuarioClientDTO = new UsuarioClientDTO();
        usuarioClientDTO.setId(1L);
        usuarioClientDTO.setNombre("Felipe");
        usuarioClientDTO.setEmail("felipe@gmail.com");
        usuarioClientDTO.setRol("CLIENTE");

        juegoClientDTO = new JuegoClientDTO();
        juegoClientDTO.setId(1L);
        juegoClientDTO.setNombre("FIFA 26");
        juegoClientDTO.setPrecio(29.99);
        juegoClientDTO.setGenero("Deportes");
        juegoClientDTO.setPlataforma("PS5");
    }

    // Método helper — simula la llamada completa a ms-usuarios via WebClient
    private void mockWebClientUsuario() {
        when(webClientUsuarios.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UsuarioClientDTO.class)).thenReturn(Mono.just(usuarioClientDTO));
    }

    // Método helper — simula la llamada completa a ms-juegos via WebClient
    private void mockWebClientJuego() {
        when(webClientJuegos.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JuegoClientDTO.class)).thenReturn(Mono.just(juegoClientDTO));
    }

    // ============================================================
    // TESTS — listarPedidos
    // ============================================================

    @Test
    void listarPedidos_debeRetornarListaConPedidos() {
        // GIVEN — el repositorio tiene un pedido guardado
        when(repository.findAll()).thenReturn(Arrays.asList(pedido));

        // WHEN — se llama al método del servicio
        List<Pedido> resultado = service.listarPedidos();

        // THEN — la lista no es nula y contiene el pedido
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
        verify(repository, times(1)).findAll();
    }

    @Test
    void listarPedidos_cuandoNoHayPedidos_debeRetornarListaVacia() {
        // GIVEN — el repositorio está vacío
        when(repository.findAll()).thenReturn(Arrays.asList());

        // WHEN — se llama al método del servicio
        List<Pedido> resultado = service.listarPedidos();

        // THEN — la lista está vacía pero no es nula
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    // ============================================================
    // TESTS — actualizarEstado
    // ============================================================

    @Test
    void actualizarEstado_cuandoEstadoValidoYPedidoExiste_debeActualizar() {
        // GIVEN — el pedido existe y el nuevo estado es válido
        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setId(1L);
        pedidoActualizado.setEstado("COMPLETADO");
        pedidoActualizado.setUsuarioId(1L);
        pedidoActualizado.setJuegoId(1L);
        pedidoActualizado.setMontoTotal(29.99);

        when(repository.findById(1L)).thenReturn(Optional.of(pedido));
        when(repository.save(any(Pedido.class))).thenReturn(pedidoActualizado);

        // WHEN — se actualiza el estado del pedido
        Pedido resultado = service.actualizarEstado(1L, "COMPLETADO");

        // THEN — el estado fue actualizado correctamente
        assertEquals("COMPLETADO", resultado.getEstado());
        verify(repository, times(1)).save(any(Pedido.class));
    }

    @Test
    void actualizarEstado_cuandoEstadoInvalido_debeLanzarExcepcion() {
        // GIVEN — el pedido existe pero el estado enviado no es válido (regla de negocio)
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));

        // WHEN + THEN — se verifica que lanza excepción por estado inválido
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarEstado(1L, "ESTADO_INVENTADO");
        });
        assertTrue(ex.getMessage().contains("Estado inválido"));

        // THEN — se verifica que NUNCA se llamó a save
        verify(repository, never()).save(any(Pedido.class));
    }

    @Test
    void actualizarEstado_cuandoPedidoYaCompletado_debeLanzarExcepcion() {
        // GIVEN — el pedido ya está COMPLETADO (regla de negocio: no se puede modificar)
        pedido.setEstado("COMPLETADO");
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));

        // WHEN + THEN — se verifica que lanza excepción por pedido ya completado
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarEstado(1L, "CANCELADO");
        });
        assertTrue(ex.getMessage().contains("No se puede modificar un pedido ya completado"));
    }

    @Test
    void actualizarEstado_cuandoPedidoNoExiste_debeLanzarExcepcion() {
        // GIVEN — no existe ningún pedido con id 99
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN — se verifica que lanza excepción con mensaje correcto
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.actualizarEstado(99L, "COMPLETADO");
        });
        assertEquals("Pedido no encontrado con id: 99", ex.getMessage());
    }

    // ============================================================
    // TESTS — eliminarPedido
    // ============================================================

    @Test
    void eliminarPedido_cuandoExiste_debeEliminarExitosamente() {
        // GIVEN — existe un pedido con id 1
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN — se elimina el pedido
        service.eliminarPedido(1L);

        // THEN — se verifica que se llamó a deleteById exactamente una vez
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarPedido_cuandoNoExiste_debeLanzarExcepcion() {
        // GIVEN — no existe ningún pedido con id 99
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN + THEN — se verifica que lanza excepción con mensaje correcto
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.eliminarPedido(99L);
        });
        assertEquals("Pedido no encontrado con id: 99", ex.getMessage());

        // THEN — se verifica que NUNCA se llamó a deleteById
        verify(repository, never()).deleteById(anyLong());
    }
}



