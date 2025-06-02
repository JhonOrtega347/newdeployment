package com.example.p_backendsigmaorder.Resena.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResenaResponse {
    private Long productoId;
    private Long usuarioId;
    private int calificacion;
    private String comentario;
    private LocalDateTime fecha;

    public ResenaResponse(Long productoId, Long usuarioId, int calificacion, String comentario, LocalDateTime fecha) {
        this.productoId = productoId;
        this.usuarioId = usuarioId;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha = fecha;
    }

}
