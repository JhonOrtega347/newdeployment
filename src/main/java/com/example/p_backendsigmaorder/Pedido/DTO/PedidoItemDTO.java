package com.example.p_backendsigmaorder.Pedido.DTO;


import lombok.Data;

import java.math.BigDecimal;
@Data
public class PedidoItemDTO {
    private Long productoId;
    private String nombre;
    private BigDecimal precioUnitario;
    private int cantidad;
    private BigDecimal subtotal;
}