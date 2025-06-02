package com.example.p_backendsigmaorder.Local.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LocalRequestDTO {




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
    private String horario;

    @NotNull(message = "Debe indicarse el ID del encargado")
    private Long encargadoId;
}
