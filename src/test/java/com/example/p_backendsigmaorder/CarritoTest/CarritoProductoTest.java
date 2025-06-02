package com.example.p_backendsigmaorder.CarritoTest;

import com.example.p_backendsigmaorder.Carrito.domain.Carrito;
import com.example.p_backendsigmaorder.Carrito.domain.CarritoProducto;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarritoProductoTest {

    private CarritoProducto carritoProducto;
    private Carrito carrito;
    private Producto producto;

    @BeforeEach
    void setUp() {
        carritoProducto = new CarritoProducto();
        carrito = new Carrito();
        producto = new Producto();

        carrito.setId(1L);
        producto.setId(1L);
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        carritoProducto.setId(id);
        assertEquals(id, carritoProducto.getId());
    }

    @Test
    void testSetAndGetCarrito() {
        carritoProducto.setCarrito(carrito);
        assertEquals(carrito, carritoProducto.getCarrito());
    }

    @Test
    void testSetAndGetProducto() {
        carritoProducto.setProducto(producto);
        assertEquals(producto, carritoProducto.getProducto());
    }

    @Test
    void testSetAndGetCantidad() {
        Integer cantidad = 5;
        carritoProducto.setCantidad(cantidad);
        assertEquals(cantidad, carritoProducto.getCantidad());
    }

    @Test
    void testConstructorVacio() {
        CarritoProducto cp = new CarritoProducto();
        assertNull(cp.getId());
        assertNull(cp.getCarrito());
        assertNull(cp.getProducto());
        assertNull(cp.getCantidad());
    }

    @Test
    void testCarritoProductoCompleto() {
        carritoProducto.setId(1L);
        carritoProducto.setCarrito(carrito);
        carritoProducto.setProducto(producto);
        carritoProducto.setCantidad(3);

        assertEquals(1L, carritoProducto.getId());
        assertEquals(carrito, carritoProducto.getCarrito());
        assertEquals(producto, carritoProducto.getProducto());
        assertEquals(3, carritoProducto.getCantidad());
    }
}