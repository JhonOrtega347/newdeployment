package com.example.p_backendsigmaorder.LocalTest;

import com.example.p_backendsigmaorder.Local.DTO.LocalRequestDTO;
import com.example.p_backendsigmaorder.Local.DTO.LocalResponseDTO;
import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Local.mapper.LocalMapper;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocalMapperTest {

    @Test
    void testToEntity() {
        LocalRequestDTO dto = new LocalRequestDTO();
        dto.setSede("Sede Central");
        dto.setDireccion("Av. Principal 123");
        dto.setTelefono("+51987654321");
        dto.setHorario("09:00–18:00");

        Usuario encargado = new Usuario();
        encargado.setId(1L);
        encargado.setNombre("Juan Pérez");

        Local local = LocalMapper.toEntity(dto, encargado);

        assertEquals("Sede Central", local.getSede());
        assertEquals("Juan Pérez", local.getEncargado().getNombre());
    }

    @Test
    void testToResponseDTO() {
        Usuario encargado = new Usuario();
        encargado.setId(1L);
        encargado.setNombre("Juan Pérez");

        Local local = new Local();
        local.setId(1L);
        local.setSede("Sede Central");
        local.setDireccion("Av. Principal 123");
        local.setTelefono("+51987654321");
        local.setHorario("09:00–18:00");
        local.setEncargado(encargado);

        LocalResponseDTO dto = LocalMapper.toResponseDTO(local);

        assertEquals("Sede Central", dto.getSede());
        assertEquals("Juan Pérez", dto.getEncargado().getNombre());
    }
}
