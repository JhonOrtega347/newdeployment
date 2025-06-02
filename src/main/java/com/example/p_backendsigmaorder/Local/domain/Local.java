package com.example.p_backendsigmaorder.Local.domain;

import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "locales")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @NotBlank(message = "La sede no puede estar vacía")
    @Size(max = 100)
    private String sede;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 255)
    private String direccion;

    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Formato de teléfono inválido"
    )
    private String telefono;

    @Size(max = 100)
    private String horario;   // ej. "09:00–18:00"


    /* RELACIONES -------------------------------------------------------- */

    // Un Local puede tener muchos Productos
    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos;

    // Esto deberia ir e producto
    //    @ManyToOne
    //    @JoinColumn(name = "local_id")
    //    private Local local;

    @ManyToOne
    @JoinColumn(name = "encargado_id", nullable = false)
    private Usuario encargado;





}
