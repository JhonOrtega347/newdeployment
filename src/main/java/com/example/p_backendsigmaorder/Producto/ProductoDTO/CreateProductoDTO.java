package com.example.p_backendsigmaorder.Producto.ProductoDTO;

import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductoDTO {
    @NotNull
    private String nombre;

    private String fechaVencimiento;

    private String descripcion;

    @NotNull
    @Positive
    private Double precio;

    @NotNull
    @Positive
    private Integer stock;

    @NotNull
    private Categoria categoria;

    private Long pedidoId;

    @NotNull
    @Positive
    private double peso;

    @NotNull
    private Long localId;  // Solo el ID del local
}