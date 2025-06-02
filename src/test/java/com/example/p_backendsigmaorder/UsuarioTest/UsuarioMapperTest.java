package com.example.p_backendsigmaorder.UsuarioTest;

import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioRequestDTO;
import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioResponseDTO;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.mapper.UsuarioMapper;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioMapperTest {

    @Test
    void testToEntity() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Lucía");
        dto.setCorreo("lucia@mail.com");
        dto.setContrasena("clave123");
        dto.setDireccion("Av. Siempre Viva");
        dto.setTelefono("+51987654321");

        Usuario usuario = UsuarioMapper.toEntity(dto);
        assertEquals("Lucía", usuario.getNombre());
    }

    @Test
    void testToResponseDTO() {
        Date now = new Date();
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Ana")
                .correo("ana@mail.com")
                .direccion("Av. Las Palmas")
                .telefono("+51911111111")
                .fechaRegistro(now)
                .rol(Rol.REPARTIDOR)
                .build();

        UsuarioResponseDTO dto = UsuarioMapper.toResponseDTO(usuario);
        assertEquals("Ana", dto.getNombre());
        assertEquals("ana@mail.com", dto.getCorreo());
        assertEquals(Rol.REPARTIDOR, dto.getRol());
    }
}