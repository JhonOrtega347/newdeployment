package com.example.p_backendsigmaorder.Local.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocalResponseDTO {

    private Long id;
    private String sede;
    private String direccion;
    private String telefono;
    private String horario;




    /* ¿Qué es EncargadoSummary?
Es un sub-objeto ligero incluido dentro de LocalResponseDTO para exponer solo los datos mínimos del encargado (por ejemplo id y nombre).
Así:

No devuelves contraseña, correo ni otros campos sensibles del Usuario.

El front puede mostrar rápidamente “Encargado: Juan Pérez (id = 7)”.

     */
    // Encargado (resumen)
    private EncargadoSummary encargado;



    @Data
    @Builder
    public static class EncargadoSummary {
        private Long id;
        private String nombre;
    }
}
