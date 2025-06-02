package com.example.p_backendsigmaorder.ProductoTest;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Producto.Exception.ProductoNotFoundException;
import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.domain.ProductoService;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_WithProducts_ReturnsProductList() {
        // Arrange
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto());
        when(productoRepository.findAll()).thenReturn(productos);

        // Act
        List<Producto> result = productoService.findAll();

        // Assert
        assertFalse(result.isEmpty());
        verify(productoRepository).findAll();
    }

    @Test
    void findAll_WithNoProducts_ThrowsException() {
        // Arrange
        when(productoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ProductoNotFoundException.class, () -> productoService.findAll());
    }

    @Test
    void findById_ExistingId_ReturnsProducto() {
        // Arrange
        Long id = 1L;
        Producto producto = new Producto();
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        // Act
        Optional<Producto> result = productoService.findById(id);

        // Assert
        assertTrue(result.isPresent());
        verify(productoRepository).findById(id);
    }

    @Test
    void findById_NonExistingId_ThrowsException() {
        // Arrange
        Long id = 1L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductoNotFoundException.class, () -> productoService.findById(id));
    }

    @Test
    void save_ValidProduct_ReturnsProduct() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("test product");
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Act
        Producto result = productoService.save(producto);

        // Assert
        assertEquals("Test Product", result.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void deleteById_ExistingId_DeletesProduct() {
        // Arrange
        Long id = 1L;
        when(productoRepository.existsById(id)).thenReturn(true);

        // Act
        productoService.deleteById(id);

        // Assert
        verify(productoRepository).deleteById(id);
    }

    @Test
    void deleteById_NonExistingId_ThrowsException() {
        // Arrange
        Long id = 1L;
        when(productoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(ProductoNotFoundException.class, () -> productoService.deleteById(id));
    }

    @Test
    void findByNombre_ExistingName_ReturnsProducts() {
        // Arrange
        String nombre = "test";
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto());
        when(productoRepository.findByNombreContainingIgnoreCase(anyString())).thenReturn(productos);

        // Act
        List<Producto> result = productoService.findByNombre(nombre);

        // Assert
        assertFalse(result.isEmpty());
        verify(productoRepository).findByNombreContainingIgnoreCase(anyString());
    }

    @Test
    void buscarPorCategoria_ExistingCategory_ReturnsProducts() {
        // Arrange
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto());
        when(productoRepository.findByCategoria(any(Categoria.class))).thenReturn(productos);

        // Act
        List<Producto> result = productoService.buscarPorCategoria(Categoria.Bebidas);

        // Assert
        assertFalse(result.isEmpty());
        verify(productoRepository).findByCategoria(Categoria.Bebidas);
    }

    @Test
    void buscarProductos_WithNameAndCategory_ReturnsFilteredProducts() {
        // Arrange
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto());
        when(productoRepository.findByNombreContainingIgnoreCaseAndCategoria(anyString(), any(Categoria.class)))
                .thenReturn(productos);

        // Act
        List<Producto> result = productoService.buscarProductos("test", Categoria.Bebidas);

        // Assert
        assertFalse(result.isEmpty());
        verify(productoRepository).findByNombreContainingIgnoreCaseAndCategoria(anyString(), any(Categoria.class));
    }

    @Test
    void obtenerProductosPorLocal_WithLocal_ReturnsProducts() {
        // Arrange
        Local local = new Local();
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto());
        when(productoRepository.findByLocal(any(Local.class))).thenReturn(productos);

        // Act
        List<Producto> result = productoService.obtenerProductosPorLocal(local);

        // Assert
        assertFalse(result.isEmpty());
        verify(productoRepository).findByLocal(local);
    }

    @Test
    void obtenerProductosPorLocalId_WithId_ReturnsProducts() {
        // Arrange
        Long localId = 1L;
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto());
        when(productoRepository.findByLocalId(anyLong())).thenReturn(productos);

        // Act
        List<Producto> result = productoService.obtenerProductosPorLocalId(localId);

        // Assert
        assertFalse(result.isEmpty());
        verify(productoRepository).findByLocalId(localId);
    }
}