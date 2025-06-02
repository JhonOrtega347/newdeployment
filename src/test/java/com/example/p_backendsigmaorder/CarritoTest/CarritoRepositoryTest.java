package com.example.p_backendsigmaorder.CarritoTest;

import com.example.p_backendsigmaorder.Carrito.domain.Carrito;
import com.example.p_backendsigmaorder.Carrito.repository.CarritoRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CarritoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarritoRepository carritoRepository;

    @Test
    @Transactional
    void findByUsuario_DeberiaEncontrarCarrito_CuandoExisteUsuario() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Test User")
                .correo("test@test.com")
                .contrasena("password123456")  // Cumple con @Size(min = 6)
                .rol(Rol.CLIENTE)             // Campo obligatorio por @Column(nullable = false)
                .build();
        // No necesitamos establecer fechaRegistro porque @PrePersist lo maneja
        
        entityManager.persist(usuario);

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        entityManager.persist(carrito);
        
        entityManager.flush();
        entityManager.clear(); // Limpia el contexto de persistencia

        // Act
        Optional<Carrito> found = carritoRepository.findByUsuario(usuario);

        // Assert
        assertAll(
            () -> assertTrue(found.isPresent(), "El carrito debería existir"),
            () -> assertNotNull(found.get().getId(), "El ID del carrito no debería ser nulo"),
            () -> assertNotNull(found.get().getUsuario(), "El usuario no debería ser nulo"),
            () -> assertEquals(usuario.getId(), found.get().getUsuario().getId(), "Los IDs de usuario deberían coincidir"),
            () -> assertEquals(usuario.getCorreo(), found.get().getUsuario().getCorreo(), "Los correos deberían coincidir"),
            () -> assertNotNull(found.get().getUsuario().getFechaRegistro(), "La fecha de registro no debería ser nula")
        );
    }

    @Test
    @Transactional
    void findByUsuario_DeberiaRetornarEmpty_CuandoNoExisteCarrito() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .nombre("Test User")
                .correo("test@test.com")
                .contrasena("password123456")
                .rol(Rol.CLIENTE)
                .build();
        
        entityManager.persist(usuario);
        entityManager.flush();
        entityManager.clear();

        // Act
        Optional<Carrito> found = carritoRepository.findByUsuario(usuario);

        // Assert
        assertTrue(found.isEmpty(), "No debería encontrar ningún carrito");
    }

    @Test
    @Transactional
    void findByUsuario_DeberiaRetornarEmpty_CuandoUsuarioEsNulo() {
        // Act
        Optional<Carrito> found = carritoRepository.findByUsuario(null);

        // Assert
        assertTrue(found.isEmpty(), "No debería encontrar ningún carrito con usuario nulo");
    }
}