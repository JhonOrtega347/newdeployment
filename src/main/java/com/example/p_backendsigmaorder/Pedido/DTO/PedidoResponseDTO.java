package com.example.p_backendsigmaorder.Pedido.DTO;



import com.example.p_backendsigmaorder.Pedido.domain.OrderStatus;
import lombok.Data;

import java.util.List;
import java.math.BigDecimal;
import java.time.Instant;


@Data
public class PedidoResponseDTO {
    private Long id;
    private List<PedidoItemDTO> items;
    private BigDecimal totalPrecio;
    private BigDecimal totalPeso;
    private OrderStatus status;
    private Instant creadoEn;
    private Instant actualizadoEn;
    // getters/setters...
}
