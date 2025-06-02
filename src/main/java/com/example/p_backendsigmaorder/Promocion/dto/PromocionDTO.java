package com.example.p_backendsigmaorder.Promocion.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromocionDTO {
    private Long id;
    private String codigoPromocion;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double porcentajeDescuento;
    private List<Long> productosIds = new ArrayList<>();

    // Campos calculados: precio original (sin descuento) y precio final (con descuento)
    private double precioOriginal;  // suma de precios de productos sin descuento
    private double precioFinal;     // precio después de aplicar descuento
    private double pesoTotal;  // nuevo campo




    // Getters y Setters
    public void setPesoTotal(double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
    public double getPesoTotal() {
        return pesoTotal;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoPromocion() { return codigoPromocion; }
    public void setCodigoPromocion(String codigoPromocion) { this.codigoPromocion = codigoPromocion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    public List<Long> getProductosIds() { return productosIds; }
    public void setProductosIds(List<Long> productosIds) { this.productosIds = productosIds; }

    public double getPrecioOriginal() { return precioOriginal; }
    public void setPrecioOriginal(double precioOriginal) { this.precioOriginal = precioOriginal; }

    public double getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(double precioFinal) { this.precioFinal = precioFinal; }
}
