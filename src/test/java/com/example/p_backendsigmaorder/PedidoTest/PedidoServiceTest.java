package com.example.p_backendsigmaorder.PedidoTest;

import com.example.p_backendsigmaorder.Carrito.DTO.CarritoProductoDTO;
import com.example.p_backendsigmaorder.Carrito.DTO.CarritoResponseDTO;
import com.example.p_backendsigmaorder.Carrito.domain.CarritoService;
import com.example.p_backendsigmaorder.Pedido.DTO.PedidoResponseDTO;
import com.example.p_backendsigmaorder.Pedido.domain.Pedido;
import com.example.p_backendsigmaorder.Pedido.domain.PedidoService;
import com.example.p_backendsigmaorder.Pedido.repository.PedidoRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private CarritoService carritoService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PedidoService pedidoService;

    private Usuario usuarioMock;
    private CarritoResponseDTO carritoResponseDTO;
    private Pedido pedidoMock;

    @BeforeEach
    void setUp() {
        // Configurar SecurityContext mock
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@test.com");

        // Crear usuario mock
        usuarioMock = Usuario.builder()
                .id(1L)
                .nombre("Usuario Test")
                .correo("test@test.com")
                .contrasena("password123")
                .rol(Rol.CLIENTE)
                .build();

        // Crear CarritoResponseDTO mock
        CarritoProductoDTO producto1 = new CarritoProductoDTO();
        producto1.setProductoId(1L);
        producto1.setNombre("Producto 1");
        producto1.setPrecio(100.0);
        producto1.setCantidad(2);

        CarritoProductoDTO producto2 = new CarritoProductoDTO();
        producto2.setProductoId(2L);
        producto2.setNombre("Producto 2");
        producto2.setPrecio(50.0);
        producto2.setCantidad(1);

        carritoResponseDTO = new CarritoResponseDTO();
        carritoResponseDTO.setProductos(Arrays.asList(producto1, producto2));
        carritoResponseDTO.setPrecioTotal(250.0);
        carritoResponseDTO.setPesoTotal(3.0);

        // Crear Pedido mock
        pedidoMock = new Pedido();
        pedidoMock.setId(1L);
        pedidoMock.setUsuario(usuarioMock);
        pedidoMock.setTotalPrecio(BigDecimal.valueOf(250.0));
        pedidoMock.setTotalPeso(BigDecimal.valueOf(3.0));
    }

    @Test
    void crearDesdeCarrito_DeberiaCrearPedidoCorrectamente() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@test.com")).thenReturn(Optional.of(usuarioMock));
        when(carritoService.obtenerCarritoDTO()).thenReturn(carritoResponseDTO);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        // Act
        PedidoResponseDTO resultado = pedidoService.crearDesdeCarrito();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertEquals(pedidoMock.getId(), resultado.getId(), "El ID del pedido debería coincidir");
        assertEquals(pedidoMock.getTotalPrecio(), resultado.getTotalPrecio(),
                "El precio total debería coincidir");
        assertEquals(pedidoMock.getTotalPeso(), resultado.getTotalPeso(),
                "El peso total debería coincidir");

        verify(carritoService, times(1)).obtenerCarritoDTO();
        verify(usuarioRepository, times(1)).findByCorreo("test@test.com");
        verify(pedidoRepository, times(2)).save(any(Pedido.class));
    }

    @Test
    void crearDesdeCarrito_CarritoVacio_DeberiaCrearPedidoVacio() {
        // Arrange
        CarritoResponseDTO carritoVacio = new CarritoResponseDTO();
        carritoVacio.setProductos(Arrays.asList());
        carritoVacio.setPrecioTotal(0.0);
        carritoVacio.setPesoTotal(0.0);

        Pedido pedidoVacio = new Pedido();
        pedidoVacio.setId(1L);
        pedidoVacio.setUsuario(usuarioMock);
        pedidoVacio.setTotalPrecio(BigDecimal.ZERO);
        pedidoVacio.setTotalPeso(BigDecimal.ZERO);

        when(usuarioRepository.findByCorreo("test@test.com")).thenReturn(Optional.of(usuarioMock));
        when(carritoService.obtenerCarritoDTO()).thenReturn(carritoVacio);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoVacio);

        // Act
        PedidoResponseDTO resultado = pedidoService.crearDesdeCarrito();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertEquals(BigDecimal.ZERO, resultado.getTotalPrecio(),
                "El precio total debería ser cero");
        assertEquals(BigDecimal.ZERO, resultado.getTotalPeso(),
                "El peso total debería ser cero");
        assertTrue(resultado.getItems().isEmpty(),
                "La lista de items debería estar vacía");
    }

    @Test
    void crearDesdeCarrito_UsuarioNoEncontrado_DeberiaLanzarException() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> pedidoService.crearDesdeCarrito(),
                "Debería lanzar RuntimeException cuando el usuario no existe");

        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void crearDesdeCarrito_CalculosCorrectos() {
        // Arrange
        when(usuarioRepository.findByCorreo("test@test.com")).thenReturn(Optional.of(usuarioMock));
        when(carritoService.obtenerCarritoDTO()).thenReturn(carritoResponseDTO);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedido = invocation.getArgument(0);
            pedido.setId(1L);
            return pedido;
        });

        // Act
        PedidoResponseDTO resultado = pedidoService.crearDesdeCarrito();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertEquals(BigDecimal.valueOf(250.0), resultado.getTotalPrecio(),
                "El precio total debería ser 250.0");
        assertEquals(2, resultado.getItems().size(),
                "Debería haber 2 items en el pedido");

        // Verificar cálculos de subtotales
        assertEquals(BigDecimal.valueOf(200.0),
                resultado.getItems().get(0).getSubtotal(),
                "El subtotal del primer item debería ser 200.0");
        assertEquals(BigDecimal.valueOf(50.0),
                resultado.getItems().get(1).getSubtotal(),
                "El subtotal del segundo item debería ser 50.0");
    }
}