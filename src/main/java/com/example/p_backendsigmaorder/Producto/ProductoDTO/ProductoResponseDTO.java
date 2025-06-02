package com.example.p_backendsigmaorder.Producto.ProductoDTO;

import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private String fechaVencimiento;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private Categoria categoria;
    private Long pedidoId;
    private double peso;
    private Long localId;
}