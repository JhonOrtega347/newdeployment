package com.example.p_backendsigmaorder.ProductoTest;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void findByNombreContainingIgnoreCase_ShouldReturnProducts() {
        // Arrange
        Producto producto1 = createProducto("Coca Cola", Categoria.Bebidas);
        Producto producto2 = createProducto("coca light", Categoria.Bebidas);
        Producto producto3 = createProducto("Pepsi", Categoria.Bebidas);

        entityManager.persist(producto1);
        entityManager.persist(producto2);
        entityManager.persist(producto3);
        entityManager.flush();

        // Act
        List<Producto> found = productoRepository.findByNombreContainingIgnoreCase("coca");

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(p -> p.getNombre().equals("Coca Cola")));
        assertTrue(found.stream().anyMatch(p -> p.getNombre().equals("coca light")));
    }

    @Test
    void findByCategoria_ShouldReturnProducts() {
        // Arrange
        Producto producto1 = createProducto("Coca Cola", Categoria.Bebidas);
        Producto producto2 = createProducto("Pasta dental", Categoria.Aseo_personal);

        entityManager.persist(producto1);
        entityManager.persist(producto2);
        entityManager.flush();

        // Act
        List<Producto> found = productoRepository.findByCategoria(Categoria.Bebidas);

        // Assert
        assertEquals(1, found.size());
        assertEquals("Coca Cola", found.get(0).getNombre());
    }

    @Test
    void findByNombreContainingIgnoreCaseAndCategoria_ShouldReturnProducts() {
        // Arrange
        Producto producto1 = createProducto("Coca Cola", Categoria.Bebidas);
        Producto producto2 = createProducto("coca light", Categoria.Bebidas);
        Producto producto3 = createProducto("Pepsi Cola", Categoria.Bebidas);

        entityManager.persist(producto1);
        entityManager.persist(producto2);
        entityManager.persist(producto3);
        entityManager.flush();

        // Act
        List<Producto> found = productoRepository.findByNombreContainingIgnoreCaseAndCategoria("coca", Categoria.Bebidas);

        // Assert
        assertEquals(2, found.size());
    }

    @Test
    void buscarProductos_ShouldReturnFilteredProducts() {
        // Arrange
        Producto producto1 = createProducto("Coca Cola", Categoria.Bebidas);
        Producto producto2 = createProducto("Pepsi", Categoria.Bebidas);
        Producto producto3 = createProducto("Chocolate", Categoria.Golosinas);

        entityManager.persist(producto1);
        entityManager.persist(producto2);
        entityManager.persist(producto3);
        entityManager.flush();

        // Act
        List<Producto> found = productoRepository.buscarProductos("coca", Categoria.Bebidas);

        // Assert
        assertEquals(1, found.size());
        assertEquals("Coca Cola", found.get(0).getNombre());
    }

    @Test
    void findByLocal_ShouldReturnProducts() {
        // Arrange
        Usuario encargado = new Usuario();
        encargado.setNombre("Encargado Test");
        encargado.setCorreo("encargado@test.com");
        encargado.setContrasena("password123");
        encargado.setRol(Rol.ENCARGADO_LOCAL);
        encargado.setFechaRegistro(new Date());
        entityManager.persist(encargado);

        Local local = new Local();
        local.setSede("Sede Test");           // Campo requerido
        local.setDireccion("Dirección Test"); // Campo requerido
        local.setEncargado(encargado);        // Campo requerido
        entityManager.persist(local);
        entityManager.flush();

        Producto producto1 = createProducto("Producto 1", Categoria.Bebidas);
        producto1.setLocal(local);
        Producto producto2 = createProducto("Producto 2", Categoria.Snacks);
        producto2.setLocal(local);

        entityManager.persist(producto1);
        entityManager.persist(producto2);
        entityManager.flush();

        // Act
        List<Producto> found = productoRepository.findByLocal(local);

        // Assert
        assertEquals(2, found.size());
    }


    private Producto createProducto(String nombre, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setPrecio(10.0);
        producto.setStock(100);
        producto.setPeso(0.5);
        return producto;
    }
}