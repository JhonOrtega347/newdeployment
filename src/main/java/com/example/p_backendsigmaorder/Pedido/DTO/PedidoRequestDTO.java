package com.example.p_backendsigmaorder.Pedido.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull private Long carritoId;
    @NotNull private Long usuarioId;
    @NotNull private List<Long> productosIds;

    @NotNull private String direccionEntrega;
    @NotNull private Date fechaEntregaEstimada;
    //@NotNull private MetodoPago metodoPago;
}
