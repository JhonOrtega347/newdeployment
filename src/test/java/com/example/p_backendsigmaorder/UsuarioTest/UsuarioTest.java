package com.example.p_backendsigmaorder.UsuarioTest;

import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    void testBuilder() {
        Date now = new Date();
        Usuario usuario = Usuario.builder()
                .nombre("Carlos")
                .correo("carlos@mail.com")
                .contrasena("secreta123")
                .direccion("Calle Falsa 123")
                .telefono("+51999999999")
                .fechaRegistro(now)
                .rol(Rol.CLIENTE)
                .build();

        assertEquals("Carlos", usuario.getNombre());
        assertEquals("carlos@mail.com", usuario.getCorreo());
        assertEquals("secreta123", usuario.getPassword());
        assertEquals("CLIENTE", usuario.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testUserDetailsMethods() {
        Usuario usuario = Usuario.builder()
                .correo("admin@mail.com")
                .contrasena("adminpass")
                .rol(Rol.ADMINISTRADOR)
                .build();

        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
        assertEquals("admin@mail.com", usuario.getUsername());
    }
}