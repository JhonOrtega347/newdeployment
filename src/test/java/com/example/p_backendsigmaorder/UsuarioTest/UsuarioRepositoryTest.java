package com.example.p_backendsigmaorder.UsuarioTest;

import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    void deberiaGuardarYRecuperarUsuarioPorCorreo() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Carlos")
                .correo("carlos@example.com")
                .contrasena("secreta123")
                .direccion("Lima")
                .telefono("+51999999999")
                .fechaRegistro(new Date())
                .rol(Rol.ADMINISTRADOR)
                .build();

        // Act
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        Optional<Usuario> usuarioRecuperado = usuarioRepository.findByCorreo("carlos@example.com");

        // Assert
        assertThat(usuarioRecuperado)
                .isPresent()
                .hasValueSatisfying(u -> {
                    assertThat(u.getNombre()).isEqualTo("Carlos");
                    assertThat(u.getCorreo()).isEqualTo("carlos@example.com");
                    assertThat(u.getRol()).isEqualTo(Rol.ADMINISTRADOR);
                    assertThat(u.getId()).isNotNull();
                });
    }

    @Test
    void deberiaRetornarVacioCuandoNoExisteElCorreo() {
        // Act
        Optional<Usuario> resultado = usuarioRepository.findByCorreo("noexiste@example.com");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void deberiaGuardarYRecuperarUsuarioPorId() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Laura")
                .correo("laura@example.com")
                .contrasena("password123")
                .direccion("Miraflores")
                .telefono("+51888888888")
                .fechaRegistro(new Date())
                .rol(Rol.CLIENTE)
                .build();

        // Act
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        Optional<Usuario> usuarioRecuperado = usuarioRepository.findById(usuarioGuardado.getId());

        // Assert
        assertThat(usuarioRecuperado)
                .isPresent()
                .hasValueSatisfying(u -> {
                    assertThat(u.getNombre()).isEqualTo("Laura");
                    assertThat(u.getCorreo()).isEqualTo("laura@example.com");
                    assertThat(u.getRol()).isEqualTo(Rol.CLIENTE);
                });
    }
}