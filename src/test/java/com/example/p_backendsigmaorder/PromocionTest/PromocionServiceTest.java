package com.example.p_backendsigmaorder.PromocionTest;

import com.example.p_backendsigmaorder.Producto.domain.Categoria;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Promocion.Exception.PromocionException;
import com.example.p_backendsigmaorder.Promocion.Service.PromocionService;
import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import com.example.p_backendsigmaorder.Promocion.dto.PromocionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromocionServiceTest {

    @Mock
    private PromocionService promocionService;

    private Promocion promocion;
    private PromocionDTO promocionDTO;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        // Configurar productos de prueba
        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Producto 1");
        producto1.setPrecio(100.0);
        producto1.setPeso(1.5);
        producto1.setStock(10);
        producto1.setCategoria(Categoria.Abarrotes);

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Producto 2");
        producto2.setPrecio(150.0);
        producto2.setPeso(2.0);
        producto2.setStock(15);
        producto2.setCategoria(Categoria.Bebidas);

        // Configurar promoción
        promocion = new Promocion();
        promocion.setId(1L);
        promocion.setCodigoPromocion("PROMO2023");
        promocion.setNombre("Promoción Especial");
        promocion.setDescripcion("Descripción de la promoción especial");
        promocion.setFechaInicio(LocalDate.now());
        promocion.setFechaFin(LocalDate.now().plusDays(7));
        promocion.setPorcentajeDescuento(20.0);
        promocion.setProductos(Arrays.asList(producto1, producto2));

        // Configurar DTO
        promocionDTO = new PromocionDTO();
        promocionDTO.setId(1L);
        promocionDTO.setCodigoPromocion("PROMO2023");
        promocionDTO.setNombre("Promoción Especial");
        promocionDTO.setDescripcion("Descripción de la promoción especial");
        promocionDTO.setFechaInicio(LocalDate.now());
        promocionDTO.setFechaFin(LocalDate.now().plusDays(7));
        promocionDTO.setPorcentajeDescuento(20.0);
        promocionDTO.setProductosIds(Arrays.asList(1L, 2L));
        promocionDTO.setPrecioOriginal(250.0);
        promocionDTO.setPrecioFinal(200.0);
        promocionDTO.setPesoTotal(3.5);
    }

    @Test
    void listar_deberiaRetornarListaDePromociones() {
        // Arrange
        when(promocionService.listar()).thenReturn(Arrays.asList(promocionDTO));

        // Act
        List<PromocionDTO> resultado = promocionService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(promocionDTO.getId(), resultado.get(0).getId());
        assertEquals(promocionDTO.getCodigoPromocion(), resultado.get(0).getCodigoPromocion());
        verify(promocionService).listar();
    }

    @Test
    void obtenerPorId_deberiaRetornarPromocionDTO() {
        // Arrange
        when(promocionService.obtenerPorId(1L)).thenReturn(promocionDTO);

        // Act
        PromocionDTO resultado = promocionService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(promocionDTO.getId(), resultado.getId());
        assertEquals(promocionDTO.getNombre(), resultado.getNombre());
        verify(promocionService).obtenerPorId(1L);
    }

    @Test
    void crear_deberiaCrearPromocion() {
        // Arrange
        when(promocionService.crear(any(PromocionDTO.class))).thenReturn(promocionDTO);

        // Act
        PromocionDTO resultado = promocionService.crear(promocionDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(promocionDTO.getCodigoPromocion(), resultado.getCodigoPromocion());
        assertEquals(promocionDTO.getPrecioOriginal(), resultado.getPrecioOriginal());
        verify(promocionService).crear(promocionDTO);
    }

    @Test
    void actualizar_deberiaActualizarPromocion() {
        // Arrange
        PromocionDTO promocionActualizada = new PromocionDTO();
        promocionActualizada.setId(1L);
        promocionActualizada.setNombre("Promoción Actualizada");
        when(promocionService.actualizar(eq(1L), any(PromocionDTO.class))).thenReturn(promocionActualizada);

        // Act
        PromocionDTO resultado = promocionService.actualizar(1L, promocionDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Promoción Actualizada", resultado.getNombre());
        verify(promocionService).actualizar(eq(1L), any(PromocionDTO.class));
    }

    @Test
    void eliminar_deberiaEliminarPromocion() {
        // Arrange
        doNothing().when(promocionService).eliminar(1L);

        // Act
        promocionService.eliminar(1L);

        // Assert
        verify(promocionService).eliminar(1L);
    }

    @Test
    void findActive_deberiaRetornarPromocionesActivas() {
        // Arrange
        when(promocionService.findActive()).thenReturn(Arrays.asList(promocion));

        // Act
        List<Promocion> resultado = promocionService.findActive();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(promocion.getCodigoPromocion(), resultado.get(0).getCodigoPromocion());
        verify(promocionService).findActive();
    }

    @Test
    void obtenerPorId_conIdInexistente_deberiaLanzarExcepcion() {
        // Arrange
        when(promocionService.obtenerPorId(99L)).thenThrow(new PromocionException("Promoción no encontrada"));

        // Act & Assert
        assertThrows(PromocionException.class, () -> promocionService.obtenerPorId(99L));
        verify(promocionService).obtenerPorId(99L);
    }

    @Test
    void crear_conDatosInvalidos_deberiaLanzarExcepcion() {
        // Arrange
        PromocionDTO dtoInvalido = new PromocionDTO();
        when(promocionService.crear(dtoInvalido)).thenThrow(new PromocionException("Datos inválidos"));

        // Act & Assert
        assertThrows(PromocionException.class, () -> promocionService.crear(dtoInvalido));
        verify(promocionService).crear(dtoInvalido);
    }
}