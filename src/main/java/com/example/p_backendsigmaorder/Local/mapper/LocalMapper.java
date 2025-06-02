package com.example.p_backendsigmaorder.Local.mapper;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Local.DTO.LocalRequestDTO;
import com.example.p_backendsigmaorder.Local.DTO.LocalResponseDTO;
import com.example.p_backendsigmaorder.Local.DTO.LocalResponseDTO.EncargadoSummary;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;

public class LocalMapper {

    /** Convierte RequestDTO + Usuario encargado a entidad Local */
    public static Local toEntity(LocalRequestDTO dto, Usuario encargado) {
        return Local.builder()
                .sede(dto.getSede())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .horario(dto.getHorario())
                .encargado(encargado)
                .build();
    }

    /** Convierte entidad Local a ResponseDTO */
    public static LocalResponseDTO toResponseDTO(Local local) {
        EncargadoSummary encargadoRes = EncargadoSummary.builder()
                .id(local.getEncargado().getId())
                .nombre(local.getEncargado().getNombre())
                .build();

        return LocalResponseDTO.builder()
                .id(local.getId())
                .sede(local.getSede())
                .direccion(local.getDireccion())
                .telefono(local.getTelefono())
                .horario(local.getHorario())
                .encargado(encargadoRes)
                .build();
    }
}

