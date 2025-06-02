package com.example.p_backendsigmaorder.Carrito.DTO;

import com.example.p_backendsigmaorder.Carrito.domain.ShippingMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CarritoResponseDTO {
    private Long carritoId;
    private List<CarritoProductoDTO> productos;
    private List<CarritoPromocionDTO> promociones;
    private double precioTotal;
    private double pesoTotal;
    private ShippingMethod shippingMethod;
    private BigDecimal shippingCost;
    private String deliveryAddress;

}