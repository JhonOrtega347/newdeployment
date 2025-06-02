package com.example.p_backendsigmaorder.CarritoTest;

import com.example.p_backendsigmaorder.Carrito.domain.Carrito;
import com.example.p_backendsigmaorder.Carrito.domain.CarritoPromocion;
import com.example.p_backendsigmaorder.Carrito.repository.CarritoPromocionRepository;
import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarritoPromocionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarritoPromocionRepository carritoPromocionRepository;

    private Usuario usuario;
    private Carrito carrito;
    private Promocion promocion;

    @BeforeEach
    void setUp() {
        // Crear y persistir Usuario
        usuario = Usuario.builder()
                .nombre("Test User")
                .correo("test@test.com")
                .contrasena("password123456")
                .rol(Rol.CLIENTE)
                .build();
        entityManager.persist(usuario);

        // Crear y persistir Carrito
        carrito = new Carrito();
        carrito.setUsuario(usuario);
        entityManager.persist(carrito);

        // Crear y persistir Promoción
        promocion = new Promocion();
        promocion.setNombre("Promoción Test");
        promocion.setCodigoPromocion("PROMO123");
        promocion.setPrecioOriginal(100.0);
        promocion.setPorcentajeDescuento(10.0);
        promocion.setFechaInicio(LocalDate.now());
        promocion.setFechaFin(LocalDate.now().plusDays(30));
        entityManager.persist(promocion);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void deberiaGuardarCarritoPromocion() {
        // Arrange
        CarritoPromocion carritoPromocion = new CarritoPromocion();
        carritoPromocion.setCarrito(carrito);
        carritoPromocion.setPromocion(promocion);
        carritoPromocion.setCantidad(1);

        // Act
        CarritoPromocion savedCarritoPromocion = carritoPromocionRepository.save(carritoPromocion);

        // Assert
        assertNotNull(savedCarritoPromocion.getId());
        assertEquals(carrito.getId(), savedCarritoPromocion.getCarrito().getId());
        assertEquals(promocion.getId(), savedCarritoPromocion.getPromocion().getId());
        assertEquals(1, savedCarritoPromocion.getCantidad());
    }

    @Test
    void deberiaEncontrarCarritoPromocionPorId() {
        // Arrange
        CarritoPromocion carritoPromocion = new CarritoPromocion();
        carritoPromocion.setCarrito(carrito);
        carritoPromocion.setPromocion(promocion);
        carritoPromocion.setCantidad(1);
        entityManager.persist(carritoPromocion);
        entityManager.flush();
        entityManager.clear();

        // Act
        Optional<CarritoPromocion> found = carritoPromocionRepository.findById(carritoPromocion.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(carritoPromocion.getId(), found.get().getId());
        assertEquals(carrito.getId(), found.get().getCarrito().getId());
        assertEquals(promocion.getId(), found.get().getPromocion().getId());
    }

    @Test
    void deberiaEliminarCarritoPromocion() {
        // Arrange
        CarritoPromocion carritoPromocion = new CarritoPromocion();
        carritoPromocion.setCarrito(carrito);
        carritoPromocion.setPromocion(promocion);
        carritoPromocion.setCantidad(1);
        entityManager.persist(carritoPromocion);
        entityManager.flush();

        // Act
        carritoPromocionRepository.deleteById(carritoPromocion.getId());
        Optional<CarritoPromocion> found = carritoPromocionRepository.findById(carritoPromocion.getId());

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void deberiaActualizarCarritoPromocion() {
        // Arrange
        CarritoPromocion carritoPromocion = new CarritoPromocion();
        carritoPromocion.setCarrito(carrito);
        carritoPromocion.setPromocion(promocion);
        carritoPromocion.setCantidad(1);
        entityManager.persist(carritoPromocion);
        entityManager.flush();
        entityManager.clear();

        // Act
        CarritoPromocion toUpdate = carritoPromocionRepository.findById(carritoPromocion.getId()).get();
        toUpdate.setCantidad(2);
        CarritoPromocion updated = carritoPromocionRepository.save(toUpdate);

        // Assert
        assertEquals(2, updated.getCantidad());
        assertEquals(carritoPromocion.getId(), updated.getId());
    }

    @Test
    void deberiaDevolverTodasLasPromocionesDelCarrito() {
        // Arrange
        CarritoPromocion carritoPromocion1 = new CarritoPromocion();
        carritoPromocion1.setCarrito(carrito);
        carritoPromocion1.setPromocion(promocion);
        carritoPromocion1.setCantidad(1);

        CarritoPromocion carritoPromocion2 = new CarritoPromocion();
        carritoPromocion2.setCarrito(carrito);
        carritoPromocion2.setPromocion(promocion);
        carritoPromocion2.setCantidad(2);

        entityManager.persist(carritoPromocion1);
        entityManager.persist(carritoPromocion2);
        entityManager.flush();
        entityManager.clear();

        // Act
        List<CarritoPromocion> promociones = carritoPromocionRepository.findAll();

        // Assert
        assertEquals(2, promociones.size());
    }

}