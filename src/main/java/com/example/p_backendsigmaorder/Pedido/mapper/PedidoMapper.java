package com.example.p_backendsigmaorder.Pedido.mapper;

import com.example.p_backendsigmaorder.Pedido.domain.Pedido;
import com.example.p_backendsigmaorder.Pedido.DTO.PedidoResponseDTO;
import com.example.p_backendsigmaorder.Pedido.DTO.PedidoItemDTO;
import com.example.p_backendsigmaorder.Pedido.domain.PedidoItem;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    public static PedidoResponseDTO toResponseDTO(Pedido p) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(p.getId());
        // Mapear items a DTO
        List<PedidoItemDTO> items = p.getItems() != null
                ? p.getItems().stream().map(PedidoMapper::toItemDTO).collect(Collectors.toList())
                : null;
        dto.setItems(items);
        dto.setTotalPrecio(p.getTotalPrecio());
        dto.setTotalPeso(p.getTotalPeso());
        dto.setStatus(p.getStatus());
        dto.setCreadoEn(p.getCreadoEn());
        dto.setActualizadoEn(p.getActualizadoEn());
        return dto;
    }

    private static PedidoItemDTO toItemDTO(PedidoItem item) {
        PedidoItemDTO dto = new PedidoItemDTO();
        dto.setProductoId(item.getProductoId());
        dto.setNombre(item.getNombre());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setCantidad(item.getCantidad());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}