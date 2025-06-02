package com.example.p_backendsigmaorder.CarritoTest;

import com.example.p_backendsigmaorder.Carrito.Controller.CarritoController;
import com.example.p_backendsigmaorder.Carrito.DTO.CarritoResponseDTO;
import com.example.p_backendsigmaorder.Carrito.domain.CarritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CarritoControllerTest {

    @Mock
    private CarritoService carritoService;

    @InjectMocks
    private CarritoController carritoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerCarrito_DeberiaRetornarCarritoDTO() {
        // Arrange
        CarritoResponseDTO expectedDTO = new CarritoResponseDTO();
        when(carritoService.obtenerCarritoDTO()).thenReturn(expectedDTO);

        // Act
        CarritoResponseDTO result = carritoController.obtenerCarrito();

        // Assert
        assertEquals(expectedDTO, result);
        verify(carritoService).obtenerCarritoDTO();
    }

    @Test
    void agregarProducto_DeberiaLlamarAlServicio() {
        // Arrange
        Long productoId = 1L;
        int cantidad = 2;

        // Act
        carritoController.agregarProducto(productoId, cantidad);

        // Assert
        verify(carritoService).agregarProducto(productoId, cantidad);
    }

    @Test
    void eliminarProducto_DeberiaLlamarAlServicio() {
        // Arrange
        Long productoId = 1L;

        // Act
        carritoController.eliminarProducto(productoId);

        // Assert
        verify(carritoService).removerProducto(productoId);
    }

    @Test
    void agregarPromocion_DeberiaLlamarAlServicio() {
        // Arrange
        Long promocionId = 1L;
        int cantidad = 2;

        // Act
        carritoController.agregarPromocion(promocionId, cantidad);

        // Assert
        verify(carritoService).agregarPromocion(promocionId, cantidad);
    }

    @Test
    void eliminarPromocion_DeberiaLlamarAlServicio() {
        // Arrange
        Long promocionId = 1L;

        // Act
        carritoController.eliminarPromocion(promocionId);

        // Assert
        verify(carritoService).removerPromocion(promocionId);
    }
}