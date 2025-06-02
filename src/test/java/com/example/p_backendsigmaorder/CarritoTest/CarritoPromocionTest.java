package com.example.p_backendsigmaorder.CarritoTest;

import com.example.p_backendsigmaorder.Carrito.domain.Carrito;
import com.example.p_backendsigmaorder.Carrito.domain.CarritoPromocion;
import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarritoPromocionTest {

    private CarritoPromocion carritoPromocion;
    private Carrito carrito;
    private Promocion promocion;

    @BeforeEach
    void setUp() {
        carritoPromocion = new CarritoPromocion();
        carrito = new Carrito();
        promocion = new Promocion();

        carrito.setId(1L);
        promocion.setId(1L);
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        carritoPromocion.setId(id);
        assertEquals(id, carritoPromocion.getId());
    }

    @Test
    void testSetAndGetCarrito() {
        carritoPromocion.setCarrito(carrito);
        assertEquals(carrito, carritoPromocion.getCarrito());
    }

    @Test
    void testSetAndGetPromocion() {
        carritoPromocion.setPromocion(promocion);
        assertEquals(promocion, carritoPromocion.getPromocion());
    }

    @Test
    void testSetAndGetCantidad() {
        Integer cantidad = 5;
        carritoPromocion.setCantidad(cantidad);
        assertEquals(cantidad, carritoPromocion.getCantidad());
    }

    @Test
    void testConstructorVacio() {
        CarritoPromocion cp = new CarritoPromocion();
        assertNull(cp.getId());
        assertNull(cp.getCarrito());
        assertNull(cp.getPromocion());
        assertNull(cp.getCantidad());
    }

    @Test
    void testCarritoPromocionCompleto() {
        carritoPromocion.setId(1L);
        carritoPromocion.setCarrito(carrito);
        carritoPromocion.setPromocion(promocion);
        carritoPromocion.setCantidad(3);

        assertEquals(1L, carritoPromocion.getId());
        assertEquals(carrito, carritoPromocion.getCarrito());
        assertEquals(promocion, carritoPromocion.getPromocion());
        assertEquals(3, carritoPromocion.getCantidad());
    }
}