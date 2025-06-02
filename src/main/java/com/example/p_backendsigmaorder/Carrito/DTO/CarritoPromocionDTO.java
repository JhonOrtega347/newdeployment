package com.example.p_backendsigmaorder.Carrito.DTO;

import lombok.Data;

@Data
public class CarritoPromocionDTO {
    private Long promocionId;
    private String titulo;
    private double precioOriginal;
    private double precioFinal;
    private double peso;
    private int cantidad;
}