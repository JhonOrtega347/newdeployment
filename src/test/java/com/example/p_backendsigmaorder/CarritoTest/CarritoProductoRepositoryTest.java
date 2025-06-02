package com.example.p_backendsigmaorder.CarritoTest;

import com.example.p_backendsigmaorder.Carrito.domain.Carrito;
import com.example.p_backendsigmaorder.Carrito.domain.CarritoProducto;
import com.example.p_backendsigmaorder.Carrito.repository.CarritoProductoRepository;
import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarritoProductoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    private Usuario usuario;
    private Carrito carrito;
    private Producto producto;

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

        // Crear y persistir Producto
        producto = new Producto();
        producto.setNombre("Producto Test");
        producto.setPrecio(10.0);
        producto.setStock(100);
        producto.setPeso(1.0);
        producto.setCategoria(Categoria.Abarrotes);
        entityManager.persist(producto);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void deberiaGuardarCarritoProducto() {
        // Arrange
        CarritoProducto carritoProducto = new CarritoProducto();
        carritoProducto.setCarrito(carrito);
        carritoProducto.setProducto(producto);
        carritoProducto.setCantidad(1);

        // Act
        CarritoProducto saved = carritoProductoRepository.save(carritoProducto);

        // Assert
        assertAll(
            () -> assertNotNull(saved.getId()),
            () -> assertEquals(carrito.getId(), saved.getCarrito().getId()),
            () -> assertEquals(producto.getId(), saved.getProducto().getId()),
            () -> assertEquals(1, saved.getCantidad())
        );
    }

    @Test
    void deberiaEncontrarCarritoProductoPorId() {
        // Arrange
        CarritoProducto carritoProducto = new CarritoProducto();
        carritoProducto.setCarrito(carrito);
        carritoProducto.setProducto(producto);
        carritoProducto.setCantidad(1);
        entityManager.persist(carritoProducto);
        entityManager.flush();
        entityManager.clear();

        // Act
        Optional<CarritoProducto> found = carritoProductoRepository.findById(carritoProducto.getId());

        // Assert
        assertTrue(found.isPresent());
        assertAll(
            () -> assertEquals(carritoProducto.getId(), found.get().getId()),
            () -> assertEquals(carrito.getId(), found.get().getCarrito().getId()),
            () -> assertEquals(producto.getId(), found.get().getProducto().getId()),
            () -> assertEquals(1, found.get().getCantidad())
        );
    }

    @Test
    void deberiaEliminarCarritoProducto() {
        // Arrange
        CarritoProducto carritoProducto = new CarritoProducto();
        carritoProducto.setCarrito(carrito);
        carritoProducto.setProducto(producto);
        carritoProducto.setCantidad(1);
        entityManager.persist(carritoProducto);
        entityManager.flush();

        // Act
        carritoProductoRepository.deleteById(carritoProducto.getId());
        Optional<CarritoProducto> found = carritoProductoRepository.findById(carritoProducto.getId());

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void deberiaActualizarCarritoProducto() {
        // Arrange
        CarritoProducto carritoProducto = new CarritoProducto();
        carritoProducto.setCarrito(carrito);
        carritoProducto.setProducto(producto);
        carritoProducto.setCantidad(1);
        entityManager.persist(carritoProducto);
        entityManager.flush();
        entityManager.clear();

        // Act
        CarritoProducto toUpdate = carritoProductoRepository.findById(carritoProducto.getId()).get();
        toUpdate.setCantidad(2);
        CarritoProducto updated = carritoProductoRepository.save(toUpdate);

        // Assert
        assertEquals(2, updated.getCantidad());
    }

    @Test
    void deberiaObtenerTodosLosCarritoProductos() {
        // Arrange
        CarritoProducto carritoProducto1 = new CarritoProducto();
        carritoProducto1.setCarrito(carrito);
        carritoProducto1.setProducto(producto);
        carritoProducto1.setCantidad(1);

        CarritoProducto carritoProducto2 = new CarritoProducto();
        carritoProducto2.setCarrito(carrito);
        carritoProducto2.setProducto(producto);
        carritoProducto2.setCantidad(2);

        entityManager.persist(carritoProducto1);
        entityManager.persist(carritoProducto2);
        entityManager.flush();
        entityManager.clear();

        // Act
        List<CarritoProducto> productos = carritoProductoRepository.findAll();

        // Assert
        assertEquals(2, productos.size());
    }

    @Test
    void noDeberiaPermitirCarritoNulo() {
        // Arrange
        CarritoProducto carritoProducto = new CarritoProducto();
        carritoProducto.setCarrito(null);
        carritoProducto.setProducto(producto);
        carritoProducto.setCantidad(1);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            carritoProductoRepository.save(carritoProducto);
            entityManager.flush();
        });
    }

    @Test
    void noDeberiaPermitirProductoNulo() {
        // Arrange
        CarritoProducto carritoProducto = new CarritoProducto();
        carritoProducto.setCarrito(carrito);
        carritoProducto.setProducto(null);
        carritoProducto.setCantidad(1);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            carritoProductoRepository.save(carritoProducto);
            entityManager.flush();
        });
    }
}