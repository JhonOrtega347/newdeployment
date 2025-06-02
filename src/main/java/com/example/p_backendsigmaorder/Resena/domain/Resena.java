package com.example.p_backendsigmaorder.Resena.domain;

import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity

//Se agrega una anotacion para evitar que
//un usuario pueda dejar mas de una reseña por producto
@Table(name = "resenas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "producto_id"}))
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Integer calificacion;

    @NotNull
    @Column(nullable = false, length = 500)
    private String comentario;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
