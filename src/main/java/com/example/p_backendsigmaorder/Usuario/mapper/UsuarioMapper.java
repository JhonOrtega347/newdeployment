package com.example.p_backendsigmaorder.Usuario.mapper;

import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioRequestDTO;
import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioResponseDTO;

public class UsuarioMapper {

    // Convierte DTO de entrada a entidad
    public static Usuario toEntity(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .nombre(dto.getNombre())
                .correo(dto.getCorreo())
                .contrasena(dto.getContrasena())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())

                .build();
    }

    // Convierte entidad a DTO de respuesta
    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .direccion(usuario.getDireccion())
                .telefono(usuario.getTelefono())
                .fechaRegistro(usuario.getFechaRegistro())
                .rol(usuario.getRol())
                .build();
    }
}
