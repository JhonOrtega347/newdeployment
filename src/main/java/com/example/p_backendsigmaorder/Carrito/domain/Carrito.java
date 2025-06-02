package com.example.p_backendsigmaorder.Carrito.domain;

import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoProducto> productos = new ArrayList<>();

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoPromocion> promociones = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShippingMethod shippingMethod = ShippingMethod.PICKUP;

    // Nuevo campo: costo de envío
    @Column(nullable = false)
    private BigDecimal shippingCost = BigDecimal.ZERO;

    // Nuevo campo: dirección para DELIVERY
    private String deliveryAddress;

}