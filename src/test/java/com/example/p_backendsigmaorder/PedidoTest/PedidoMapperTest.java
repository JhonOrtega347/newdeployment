package com.example.p_backendsigmaorder.PedidoTest;

import com.example.p_backendsigmaorder.Pedido.DTO.PedidoItemDTO;
import com.example.p_backendsigmaorder.Pedido.DTO.PedidoResponseDTO;
import com.example.p_backendsigmaorder.Pedido.domain.OrderStatus;
import com.example.p_backendsigmaorder.Pedido.domain.Pedido;
import com.example.p_backendsigmaorder.Pedido.domain.PedidoItem;
import com.example.p_backendsigmaorder.Pedido.mapper.PedidoMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoMapperTest {

    @Test
    void toResponseDTO_conPedidoCompleto_deberiaMapearCorrectamente() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setTotalPrecio(BigDecimal.valueOf(100.0));
        pedido.setTotalPeso(BigDecimal.valueOf(2.5));
        pedido.setStatus(OrderStatus.CREATED);

        Instant ahora = Instant.now();
        pedido.setCreadoEn(ahora);
        pedido.setActualizadoEn(ahora);

        List<PedidoItem> items = new ArrayList<>();
        PedidoItem item = new PedidoItem();
        item.setProductoId(1L);
        item.setNombre("Producto Test");
        item.setPrecioUnitario(BigDecimal.valueOf(50.0));
        item.setCantidad(2);
        item.setSubtotal(BigDecimal.valueOf(100.0));
        items.add(item);
        pedido.setItems(items);

        // Act
        PedidoResponseDTO resultado = PedidoMapper.toResponseDTO(pedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(BigDecimal.valueOf(100.0), resultado.getTotalPrecio());
        assertEquals(BigDecimal.valueOf(2.5), resultado.getTotalPeso());
        assertEquals(OrderStatus.CREATED, resultado.getStatus());
        assertEquals(ahora, resultado.getCreadoEn());
        assertEquals(ahora, resultado.getActualizadoEn());

        // Verificar items
        assertNotNull(resultado.getItems());
        assertEquals(1, resultado.getItems().size());

        PedidoItemDTO itemDTO = resultado.getItems().get(0);
        assertEquals(1L, itemDTO.getProductoId());
        assertEquals("Producto Test", itemDTO.getNombre());
        assertEquals(BigDecimal.valueOf(50.0), itemDTO.getPrecioUnitario());
        assertEquals(2, itemDTO.getCantidad());
        assertEquals(BigDecimal.valueOf(100.0), itemDTO.getSubtotal());
    }

    @Test
    void toResponseDTO_sinItems_deberiaMapearCorrectamente() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setTotalPrecio(BigDecimal.valueOf(0.0));
        pedido.setTotalPeso(BigDecimal.valueOf(0.0));
        pedido.setStatus(OrderStatus.CREATED);
        pedido.setItems(null);

        // Act
        PedidoResponseDTO resultado = PedidoMapper.toResponseDTO(pedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertNull(resultado.getItems());
        assertEquals(BigDecimal.valueOf(0.0), resultado.getTotalPrecio());
        assertEquals(BigDecimal.valueOf(0.0), resultado.getTotalPeso());
        assertEquals(OrderStatus.CREATED, resultado.getStatus());
    }

    @Test
    void toResponseDTO_conListaItemsVacia_deberiaMapearCorrectamente() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setItems(new ArrayList<>());

        // Act
        PedidoResponseDTO resultado = PedidoMapper.toResponseDTO(pedido);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getItems());
        assertTrue(resultado.getItems().isEmpty());
    }

    @Test
    void toResponseDTO_conValoresNulos_deberiaMapearSinErrores() {
        // Arrange
        Pedido pedido = new Pedido();
        // No establecemos ningún valor, todos quedarán null

        // Act
        PedidoResponseDTO resultado = PedidoMapper.toResponseDTO(pedido);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getId());
        assertNull(resultado.getItems());
        assertNull(resultado.getTotalPrecio());
        assertNull(resultado.getTotalPeso());
    }
}