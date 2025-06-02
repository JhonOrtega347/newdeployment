package com.example.p_backendsigmaorder.CarritoTest;

import com.example.p_backendsigmaorder.Carrito.DTO.CarritoResponseDTO;
import com.example.p_backendsigmaorder.Carrito.domain.*;
import com.example.p_backendsigmaorder.Carrito.repository.*;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import com.example.p_backendsigmaorder.Promocion.Repository.PromocionRepository;
import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private CarritoProductoRepository carritoProductoRepository;

    @Mock
    private CarritoPromocionRepository carritoPromocionRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PromocionRepository promocionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CarritoService carritoService;

    private Usuario usuario;
    private Carrito carrito;
    private Producto producto;
    private Promocion promocion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@test.com");

        // Setup test data
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("test@test.com");

        carrito = new Carrito();
        carrito.setId(1L);
        carrito.setUsuario(usuario);
        carrito.setProductos(new ArrayList<>());
        carrito.setPromociones(new ArrayList<>());

        producto = new Producto();
        producto.setId(1L);
        producto.setPrecio(100.0);
        producto.setPeso(1.0);

        promocion = new Promocion();
        promocion.setId(1L);
        promocion.setPrecioFinal(90.0);
        promocion.setProductos(new ArrayList<>());

        when(usuarioRepository.findByCorreo("test@test.com")).thenReturn(Optional.of(usuario));
        when(carritoRepository.findByUsuario(usuario)).thenReturn(Optional.of(carrito));
    }

    @Test
    void getCarritoActual_DeberiaRetornarCarritoExistente() {
        Carrito result = carritoService.getCarritoActual();

        assertEquals(carrito, result);
        verify(carritoRepository).findByUsuario(usuario);
    }

    @Test
    void agregarProducto_DeberiaAgregarNuevoProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        carritoService.agregarProducto(1L, 2);

        verify(carritoProductoRepository).save(any(CarritoProducto.class));
    }

    @Test
    void agregarPromocion_DeberiaAgregarNuevaPromocion() {
        when(promocionRepository.findById(1L)).thenReturn(Optional.of(promocion));

        carritoService.agregarPromocion(1L, 2);

        verify(carritoPromocionRepository).save(any(CarritoPromocion.class));
    }

    @Test
    void obtenerCarritoDTO_DeberiaRetornarDTOConDatosCorrectos() {
        CarritoResponseDTO dto = carritoService.obtenerCarritoDTO();

        assertNotNull(dto);
        assertEquals(carrito.getId(), dto.getCarritoId());
    }

    @Test
    void removerProducto_DeberiaEliminarProductoDelCarrito() {
        carritoService.removerProducto(1L);

        assertTrue(carrito.getProductos().isEmpty());
    }

    @Test
    void removerPromocion_DeberiaEliminarPromocionDelCarrito() {
        carritoService.removerPromocion(1L);

        assertTrue(carrito.getPromociones().isEmpty());
    }
}
