package com.example.p_backendsigmaorder.ProductoTest;

import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Producto.ProductoDTO.CreateProductoDTO;
import com.example.p_backendsigmaorder.Producto.ProductoDTO.ProductoResponseDTO;
import com.example.p_backendsigmaorder.Producto.ProductoMapper.ProductoMapper;
import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductoMapperTest {

    private ProductoMapper productoMapper;

    @BeforeEach
    void setUp() {
        productoMapper = new ProductoMapper();
    }

    @Test
    void toEntity_ShouldMapDTOToEntity() {
        // Arrange
        CreateProductoDTO dto = new CreateProductoDTO();
        dto.setNombre("Test Product");
        dto.setFechaVencimiento("2024-12-31");
        dto.setDescripcion("Test Description");
        dto.setPrecio(100.0);
        dto.setStock(50);
        dto.setCategoria(Categoria.Bebidas);
        dto.setPedidoId(1L);
        dto.setPeso(0.5);
        dto.setLocalId(1L);

        // Act
        Producto producto = productoMapper.toEntity(dto);

        // Assert
        assertNotNull(producto);
        assertEquals(dto.getNombre(), producto.getNombre());
        assertEquals(dto.getFechaVencimiento(), producto.getFechaVencimiento());
        assertEquals(dto.getDescripcion(), producto.getDescripcion());
        assertEquals(dto.getPrecio(), producto.getPrecio());
        assertEquals(dto.getStock(), producto.getStock());
        assertEquals(dto.getCategoria(), producto.getCategoria());
        assertEquals(dto.getPedidoId(), producto.getPedidoId());
        assertEquals(dto.getPeso(), producto.getPeso());
        assertEquals(dto.getLocalId(), producto.getLocal().getId());
    }

    @Test
    void toDTO_ShouldMapEntityToDTO() {
        // Arrange
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Test Product");
        producto.setFechaVencimiento("2024-12-31");
        producto.setDescripcion("Test Description");
        producto.setPrecio(100.0);
        producto.setStock(50);
        producto.setCategoria(Categoria.Bebidas);
        producto.setPedidoId(1L);
        producto.setPeso(0.5);

        Local local = new Local();
        local.setId(1L);
        producto.setLocal(local);

        // Act
        ProductoResponseDTO dto = productoMapper.toDTO(producto);

        // Assert
        assertNotNull(dto);
        assertEquals(producto.getId(), dto.getId());
        assertEquals(producto.getNombre(), dto.getNombre());
        assertEquals(producto.getFechaVencimiento(), dto.getFechaVencimiento());
        assertEquals(producto.getDescripcion(), dto.getDescripcion());
        assertEquals(producto.getPrecio(), dto.getPrecio());
        assertEquals(producto.getStock(), dto.getStock());
        assertEquals(producto.getCategoria(), dto.getCategoria());
        assertEquals(producto.getPedidoId(), dto.getPedidoId());
        assertEquals(producto.getPeso(), dto.getPeso());
        assertEquals(producto.getLocal().getId(), dto.getLocalId());
    }
}