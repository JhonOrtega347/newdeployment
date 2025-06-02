package com.example.p_backendsigmaorder.ProductoTest;

import com.example.p_backendsigmaorder.Producto.Controller.ProductoController;
import com.example.p_backendsigmaorder.Producto.ProductoDTO.CreateProductoDTO;
import com.example.p_backendsigmaorder.Producto.ProductoDTO.ProductoResponseDTO;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.ProductoMapper.ProductoMapper;
import com.example.p_backendsigmaorder.Producto.domain.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductoControllerTest {

    private ProductoService productoService;
    private ProductoMapper productoMapper;
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        productoService = mock(ProductoService.class);
        productoMapper = mock(ProductoMapper.class);
        productoController = new ProductoController(productoService, productoMapper);
    }

    @Test
    void getAllProductos_ReturnsListOfProductos() {
        Producto producto = new Producto();
        ProductoResponseDTO dto = new ProductoResponseDTO();

        when(productoService.findAll()).thenReturn(Arrays.asList(producto));
        when(productoMapper.toDTO(any(Producto.class))).thenReturn(dto);

        ResponseEntity<List<ProductoResponseDTO>> response = productoController.getAllProductos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getProductoById_ExistingId_ReturnsProducto() {
        Long id = 1L;
        Producto producto = new Producto();
        ProductoResponseDTO dto = new ProductoResponseDTO();

        when(productoService.findById(id)).thenReturn(Optional.of(producto));
        when(productoMapper.toDTO(producto)).thenReturn(dto);

        ResponseEntity<ProductoResponseDTO> response = productoController.getProductoById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createProducto_ValidDTO_ReturnsCreatedProducto() {
        CreateProductoDTO createDTO = new CreateProductoDTO();
        Producto producto = new Producto();
        ProductoResponseDTO responseDTO = new ProductoResponseDTO();

        when(productoMapper.toEntity(createDTO)).thenReturn(producto);
        when(productoService.save(producto)).thenReturn(producto);
        when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

        ResponseEntity<ProductoResponseDTO> response = productoController.createProducto(createDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateProducto_ExistingId_ReturnsUpdatedProducto() {
        Long id = 1L;
        CreateProductoDTO updateDTO = new CreateProductoDTO();
        Producto producto = new Producto();
        ProductoResponseDTO responseDTO = new ProductoResponseDTO();

        when(productoService.findById(id)).thenReturn(Optional.of(producto));
        when(productoMapper.toEntity(updateDTO)).thenReturn(producto);
        when(productoService.save(producto)).thenReturn(producto);
        when(productoMapper.toDTO(producto)).thenReturn(responseDTO);

        ResponseEntity<ProductoResponseDTO> response = productoController.updateProducto(id, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteProducto_ExistingId_ReturnsNoContent() {
        Long id = 1L;
        Producto producto = new Producto();

        when(productoService.findById(id)).thenReturn(Optional.of(producto));

        ResponseEntity<Void> response = productoController.deleteProducto(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productoService).deleteById(id);
    }
}