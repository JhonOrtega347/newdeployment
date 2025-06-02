package com.example.p_backendsigmaorder.ResenaTest;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Resena.domain.Resena;
import com.example.p_backendsigmaorder.Resena.repository.ResenaRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ResenaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResenaRepository resenaRepositoryTest;

    private Usuario createEncargado() {
        Usuario encargado = new Usuario();
        encargado.setNombre("Encargado Test");
        encargado.setCorreo("encargado@test.com");
        encargado.setContrasena("password");
        encargado.setDireccion("Dirección del encargado");
        encargado.setTelefono("+123456789");
        encargado.setFechaRegistro(new Date());
        encargado.setRol(Rol.ENCARGADO_LOCAL);
        return entityManager.persist(encargado);
    }

    private Local createLocal(Usuario encargado) {
        Local local = Local.builder()
                .sede("Sede Test")
                .direccion("Dirección Test")
                .telefono("+123456789")
                .horario("09:00-18:00")
                .encargado(encargado)
                .productos(new ArrayList<>())
                .build();
        return entityManager.persist(local);
    }

    private Producto createProducto() {
        Usuario encargado = createEncargado();
        Local local = createLocal(encargado);

        Producto producto = new Producto();
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción de prueba");
        producto.setPrecio(100.0);
        producto.setStock(10);
        producto.setCategoria(Categoria.Abarrotes);
        producto.setPeso(1.0);
        producto.setLocal(local);
        producto.setFechaVencimiento("2024-12-31");

        Producto savedProducto = entityManager.persist(producto);

        // Actualizar la lista de productos del local
        local.getProductos().add(savedProducto);
        entityManager.merge(local);

        return savedProducto;
    }

    private Usuario createUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Test");
        usuario.setCorreo("cliente@test.com");
        usuario.setContrasena("password");
        usuario.setDireccion("Dirección de prueba");
        usuario.setTelefono("+987654321");
        usuario.setFechaRegistro(new Date());
        usuario.setRol(Rol.CLIENTE);
        return entityManager.persist(usuario);
    }

    private Usuario createUsuario2() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Usuario Test2");
        usuario.setCorreo("cliente@test2.com");
        usuario.setContrasena("password2");
        usuario.setDireccion("Dirección de prueba 2");
        usuario.setTelefono("+987654322");
        usuario.setFechaRegistro(new Date());
        usuario.setRol(Rol.CLIENTE);
        return entityManager.persist(usuario);
    }

    private Resena createResena(Usuario usuario, Producto producto) {
        Resena resena = new Resena();
        resena.setUsuario(usuario);
        resena.setProducto(producto);
        resena.setCalificacion(5);
        resena.setComentario("Excelente producto");
        resena.setFecha(LocalDateTime.now());
        return entityManager.persist(resena);
    }

    @Test
    void findByProductoId_DeberiaEncontrarResenasDelProducto() {
        // Arrange
        Producto producto = createProducto();
        Usuario usuario1 = createUsuario();
        Usuario usuario2 = createUsuario2();

        Resena resena1 = createResena(usuario1, producto);
        Resena resena2 = createResena(usuario2, producto);

        entityManager.flush();

        // Act
        List<Resena> resenas = resenaRepositoryTest.findByProductoId(producto.getId());

        // Assert
        assertEquals(2, resenas.size());
        assertTrue(resenas.stream().allMatch(r -> r.getProducto().getId().equals(producto.getId())));
    }

    @Test
    void existsByUsuarioIdAndProductoId_DeberiaRetornarTrue_CuandoExisteResena() {
        // Arrange
        Usuario usuario = createUsuario();
        Producto producto = createProducto();
        Resena resena = createResena(usuario, producto);

        entityManager.flush();

        // Act
        boolean exists = resenaRepositoryTest.existsByUsuarioIdAndProductoId(
                usuario.getId(),
                producto.getId()
        );

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByUsuarioIdAndProductoId_DeberiaRetornarFalse_CuandoNoExisteResena() {
        // Arrange
        Usuario usuario = createUsuario();
        Producto producto = createProducto();

        entityManager.flush();

        // Act
        boolean exists = resenaRepositoryTest.existsByUsuarioIdAndProductoId(
                usuario.getId(),
                producto.getId()
        );

        // Assert
        assertFalse(exists);
    }

    @Test
    void save_DeberiaGuardarNuevaResena() {
        // Arrange
        Usuario usuario = createUsuario();
        Producto producto = createProducto();
        Resena resena = new Resena();
        resena.setUsuario(usuario);
        resena.setProducto(producto);
        resena.setCalificacion(5);
        resena.setComentario("Excelente producto");
        resena.setFecha(LocalDateTime.now());

        // Act
        Resena savedResena = resenaRepositoryTest.save(resena);

        // Assert
        assertNotNull(savedResena.getId());
        assertEquals(resena.getCalificacion(), savedResena.getCalificacion());
        assertEquals(resena.getComentario(), savedResena.getComentario());
    }

    @Test
    void delete_DeberiaEliminarResena() {
        // Arrange
        Usuario usuario = createUsuario();
        Producto producto = createProducto();
        Resena resena = createResena(usuario, producto);
        entityManager.flush();

        // Act
        resenaRepositoryTest.delete(resena);
        entityManager.flush();

        // Assert
        assertFalse(resenaRepositoryTest.findById(resena.getId()).isPresent());
    }
}