package com.example.p_backendsigmaorder.Pedido.domain;

import com.example.p_backendsigmaorder.Carrito.domain.Carrito;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
@Data
@Entity
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoItem> items;

    private BigDecimal totalPrecio;
    private BigDecimal totalPeso;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    private Instant creadoEn = Instant.now();
    private Instant actualizadoEn = Instant.now();

    // getters y setters...
    @PreUpdate
    public void preUpdate() {
        actualizadoEn = Instant.now();
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<PedidoItem> getItems() { return items; }
    public void setItems(List<PedidoItem> items) { this.items = items; }
    public BigDecimal getTotalPrecio() { return totalPrecio; }
    public void setTotalPrecio(BigDecimal totalPrecio) { this.totalPrecio = totalPrecio; }
    public BigDecimal getTotalPeso() { return totalPeso; }
    public void setTotalPeso(BigDecimal totalPeso) { this.totalPeso = totalPeso; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public Instant getCreadoEn() { return creadoEn; }
    public Instant getActualizadoEn() { return actualizadoEn; }
}