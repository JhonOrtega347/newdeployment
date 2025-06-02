package com.example.p_backendsigmaorder.Promocion.domain;

import com.example.p_backendsigmaorder.Producto.domain.Producto;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoPromocion;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(nullable = false)
    private double precioOriginal; // Persistido en DB: suma precios productos antes del descuento

    @ManyToMany
    @JoinTable(
            name = "promocion_producto",
            joinColumns = @JoinColumn(name = "promocion_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos;

    @Column(nullable = false)
    private double porcentajeDescuento;

    @Transient // No persistido en DB: calculado al vuelo
    private double precioFinal;

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigoPromocion(String codigoPromocion) {
        this.codigoPromocion = codigoPromocion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setPrecioOriginal(double precioOriginal) {
        this.precioOriginal = precioOriginal;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    // Getters (además de los generados por Lombok con @Getter)

    public double getPrecioFinal() {
        // Calcula el precio final al vuelo
        double total = (productos == null) ? 0 : productos.stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
        return total - (total * porcentajeDescuento / 100.0);
    }
}
