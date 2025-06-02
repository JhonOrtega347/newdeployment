package com.example.p_backendsigmaorder.Carrito.DTO;

import com.example.p_backendsigmaorder.Carrito.domain.ShippingMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class MetodoEnvioDTO {

    @NotNull(message = "Debe indicar el método de envío")
    private ShippingMethod shippingMethod;

    /**
     * Obligatorio solo si shippingMethod == DELIVERY.
     * Para PICKUP se ignora.
     */
    private String deliveryAddress;

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

}
