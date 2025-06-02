package com.example.p_backendsigmaorder.PedidoTest;

import com.example.p_backendsigmaorder.Pedido.domain.OrderStatus;
import com.example.p_backendsigmaorder.Pedido.domain.Pedido;
import com.example.p_backendsigmaorder.Pedido.repository.PedidoRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Usuario crearUsuarioValido(String nombre, String correo, Rol rol) {
        return Usuario.builder()
                .nombre(nombre)
                .correo(correo)
                .contrasena("password123")
                .rol(rol)
                .telefono("+51987654321")
                .build();
    }

    @Test
    void findByUsuario_ConPedidosExistentes_DeberiaRetornarPedidosDelUsuario() {
        // Arrange
        Usuario usuario1 = crearUsuarioValido("Usuario 1", "usuario1@test.com", Rol.CLIENTE);
        entityManager.persist(usuario1);

        Usuario usuario2 = crearUsuarioValido("Usuario 2", "usuario2@test.com", Rol.CLIENTE);
        entityManager.persist(usuario2);

        Pedido pedido1Usuario1 = new Pedido();
        pedido1Usuario1.setUsuario(usuario1);
        pedido1Usuario1.setTotalPrecio(new BigDecimal("100.00"));
        entityManager.persist(pedido1Usuario1);

        Pedido pedido2Usuario1 = new Pedido();
        pedido2Usuario1.setUsuario(usuario1);
        pedido2Usuario1.setTotalPrecio(new BigDecimal("200.00"));
        entityManager.persist(pedido2Usuario1);

        Pedido pedidoUsuario2 = new Pedido();
        pedidoUsuario2.setUsuario(usuario2);
        pedidoUsuario2.setTotalPrecio(new BigDecimal("150.00"));
        entityManager.persist(pedidoUsuario2);

        entityManager.flush();

        // Act
        List<Pedido> pedidosUsuario1 = pedidoRepository.findByUsuario(usuario1);

        // Assert
        assertNotNull(pedidosUsuario1, "La lista de pedidos no debería ser null");
        assertEquals(2, pedidosUsuario1.size(), "Debería haber 2 pedidos para usuario1");
        assertTrue(pedidosUsuario1.stream().allMatch(p -> p.getUsuario().equals(usuario1)),
                "Todos los pedidos deberían pertenecer a usuario1");
    }

    @Test
    void findByUsuario_UsuariosConDiferentesRoles_DeberiaRetornarPedidosCorrectos() {
        // Arrange
        Usuario cliente = crearUsuarioValido("Cliente Test", "cliente@test.com", Rol.CLIENTE);
        Usuario admin = crearUsuarioValido("Admin Test", "admin@test.com", Rol.ADMINISTRADOR);
        Usuario repartidor = crearUsuarioValido("Repartidor Test", "repartidor@test.com", Rol.REPARTIDOR);
        Usuario encargado = crearUsuarioValido("Encargado Test", "encargado@test.com", Rol.ENCARGADO_LOCAL);

        entityManager.persist(cliente);
        entityManager.persist(admin);
        entityManager.persist(repartidor);
        entityManager.persist(encargado);

        Pedido pedidoCliente = new Pedido();
        pedidoCliente.setUsuario(cliente);
        pedidoCliente.setTotalPrecio(new BigDecimal("100.00"));
        entityManager.persist(pedidoCliente);

        Pedido pedidoAdmin = new Pedido();
        pedidoAdmin.setUsuario(admin);
        pedidoAdmin.setTotalPrecio(new BigDecimal("200.00"));
        entityManager.persist(pedidoAdmin);

        entityManager.flush();

        // Act & Assert
        List<Pedido> pedidosCliente = pedidoRepository.findByUsuario(cliente);
        List<Pedido> pedidosAdmin = pedidoRepository.findByUsuario(admin);
        List<Pedido> pedidosRepartidor = pedidoRepository.findByUsuario(repartidor);

        assertEquals(1, pedidosCliente.size(), "Cliente debería tener 1 pedido");
        assertEquals(1, pedidosAdmin.size(), "Admin debería tener 1 pedido");
        assertEquals(0, pedidosRepartidor.size(), "Repartidor no debería tener pedidos");
    }

    @Test
    void findByUsuario_SinPedidos_DeberiaRetornarListaVacia() {
        // Arrange
        Usuario usuario = crearUsuarioValido("Usuario Sin Pedidos", "sinpedidos@test.com", Rol.CLIENTE);
        entityManager.persist(usuario);
        entityManager.flush();

        // Act
        List<Pedido> pedidos = pedidoRepository.findByUsuario(usuario);

        // Assert
        assertNotNull(pedidos, "La lista no debería ser null");
        assertTrue(pedidos.isEmpty(), "La lista debería estar vacía");
    }

    @Test
    void save_PedidoNuevo_DeberiaGuardarCorrectamente() {
        // Arrange
        Usuario usuario = crearUsuarioValido("Usuario Test", "test@test.com", Rol.CLIENTE);
        entityManager.persist(usuario);

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setUsuario(usuario);
        nuevoPedido.setTotalPrecio(new BigDecimal("300.00"));
        nuevoPedido.setStatus(OrderStatus.CREATED);

        // Act
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);

        // Assert
        assertNotNull(pedidoGuardado.getId(), "El ID no debería ser null después de guardar");
        assertEquals(nuevoPedido.getTotalPrecio(), pedidoGuardado.getTotalPrecio(),
                "El precio total debería ser el mismo");
        assertEquals(nuevoPedido.getUsuario(), pedidoGuardado.getUsuario(),
                "El usuario debería ser el mismo");
        assertEquals(Rol.CLIENTE, pedidoGuardado.getUsuario().getRol(),
                "El rol del usuario debería mantenerse como CLIENTE");
    }

    @Test
    void findById_PedidoExistente_DeberiaRetornarPedido() {
        // Arrange
        Usuario usuario = crearUsuarioValido("Usuario Test", "test@test.com", Rol.CLIENTE);
        entityManager.persist(usuario);

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTotalPrecio(new BigDecimal("400.00"));
        entityManager.persist(pedido);
        entityManager.flush();

        // Act
        Optional<Pedido> pedidoEncontrado = pedidoRepository.findById(pedido.getId());

        // Assert
        assertTrue(pedidoEncontrado.isPresent(), "El pedido debería existir");
        assertEquals(pedido.getId(), pedidoEncontrado.get().getId(),
                "Los IDs deberían coincidir");
        assertEquals(pedido.getTotalPrecio(), pedidoEncontrado.get().getTotalPrecio(),
                "Los precios totales deberían coincidir");
    }
}