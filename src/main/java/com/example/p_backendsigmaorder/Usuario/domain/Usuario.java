package com.example.p_backendsigmaorder.Usuario.domain;

import com.example.p_backendsigmaorder.Local.domain.Local;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Formato de correo inválido")
    @Column(unique = true, nullable = false)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

    @Size(max = 255)
    private String direccion;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Formato de teléfono inválido")
    private String telefono;

    @NotNull(message = "La fecha de registro no puede ser nula")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;




    //Con esto, cada vez que crees un nuevo usuario y lo guardes en la base de datos,
    // fechaRegistro se asignará automáticamente.
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = new Date();
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() ->  rol.name());}

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // O lógica propia si tienes
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // O lógica propia si tienes
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // O lógica propia si tienes
    }

    @Override
    public boolean isEnabled() {
        return true; // O lógica propia si tienes
    }

    @ManyToOne
    @JoinColumn(name = "local_id")
    private Local local;


}