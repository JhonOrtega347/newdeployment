package com.example.p_backendsigmaorder.Carrito.DTO;

import lombok.Data;

@Data
public class CarritoProductoDTO {
    private Long productoId;
    private String nombre;
    private double precio;
    private double peso;
    private int cantidad;
}