package com.example.p_backendsigmaorder.Usuario.DTO;

import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
    private Date fechaRegistro;
    private Rol rol;
}
