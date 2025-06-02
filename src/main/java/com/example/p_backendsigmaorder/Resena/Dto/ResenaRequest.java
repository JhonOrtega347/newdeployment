package com.example.p_backendsigmaorder.Resena.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResenaRequest {
    @NotNull
    private Long productoId;

    private Long usuarioId;
    @NotNull
    @Min(value = 1, message = "La calificación minima es 1")
    @Max(value = 5, message = "La calificación maxima es 5")
    private Integer calificacion;
    @NotNull
    private String comentario;


}
