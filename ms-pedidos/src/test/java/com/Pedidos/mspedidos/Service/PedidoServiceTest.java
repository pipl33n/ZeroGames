package com.Pedidos.mspedidos.Service;


import com.Pedidos.mspedidos.DTO.JuegoClientDTO;
import com.Pedidos.mspedidos.DTO.PedidoDTO;
import com.Pedidos.mspedidos.DTO.UsuarioClientDTO;
import com.Pedidos.mspedidos.Model.Pedido;
import com.Pedidos.mspedidos.Repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @MockitoBean
    private PedidoRepository pedidoRepository;

    @MockitoBean(name = "webClientJuegos")
    private WebClient webClientJuegos;

    @MockitoBean(name = "webClientUsuarios")
    private WebClient webClientUsuarios;

    @MockitoBean
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @MockitoBean
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @MockitoBean
    private WebClient.ResponseSpec responseSpec;

    // Pedido(Long id, Long usuarioId, Long juegoId, Double montoTotal, String estado)
    // UsuarioClientDTO y JuegoClientDTO solo tienen @Data, usan setters

    @Test
    public void testListarPedidos() {
        when(pedidoRepository.findAll()).thenReturn(List.of(new Pedido(1L, 1L, 1L, 29.99, "PENDIENTE")));

        List<Pedido> resultado = pedidoService.listarPedidos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
    }

    @Test
    public void testListarPedidos_vacio() {
        when(pedidoRepository.findAll()).thenReturn(List.of());

        List<Pedido> resultado = pedidoService.listarPedidos();
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    public void testActualizarEstado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(new Pedido(1L, 1L, 1L, 29.99, "PENDIENTE")));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(new Pedido(1L, 1L, 1L, 29.99, "COMPLETADO"));

        Pedido resultado = pedidoService.actualizarEstado(1L, "COMPLETADO");
        assertNotNull(resultado);
        assertEquals("COMPLETADO", resultado.getEstado());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    public void testActualizarEstado_estadoInvalido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(new Pedido(1L, 1L, 1L, 29.99, "PENDIENTE")));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.actualizarEstado(1L, "ESTADO_INVENTADO");
        });
        assertTrue(ex.getMessage().contains("Estado inválido"));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    public void testActualizarEstado_pedidoNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.actualizarEstado(99L, "COMPLETADO");
        });
        assertEquals("Pedido no encontrado con id: 99", ex.getMessage());
    }

    @Test
    public void testEliminarPedido() {
        when(pedidoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(1L);

        pedidoService.eliminarPedido(1L);
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testEliminarPedido_noExiste() {
        when(pedidoRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.eliminarPedido(99L);
        });
        assertEquals("Pedido no encontrado con id: 99", ex.getMessage());
        verify(pedidoRepository, never()).deleteById(anyLong());
    }
}