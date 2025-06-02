package com.example.p_backendsigmaorder.PromocionTest;

import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import com.example.p_backendsigmaorder.Promocion.Exception.PromocionException;
import com.example.p_backendsigmaorder.Promocion.Repository.PromocionRepository;
import com.example.p_backendsigmaorder.Promocion.Service.PromocionServiceImplem;
import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import com.example.p_backendsigmaorder.Promocion.dto.PromocionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PromocionServiceImplemTest {

    @Mock
    private PromocionRepository promocionRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private PromocionServiceImplem promocionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_DebeRetornarListaPromociones() {
        // Arrange
        Promocion promocion1 = new Promocion();
        promocion1.setId(1L);
        promocion1.setProductos(List.of());

        Promocion promocion2 = new Promocion();
        promocion2.setId(2L);
        promocion2.setProductos(List.of());

        when(promocionRepository.findAll()).thenReturn(Arrays.asList(promocion1, promocion2));

        // Act
        List<PromocionDTO> resultado = promocionService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(promocionRepository).findAll();
    }

    @Test
    void obtenerPorId_CuandoExisteId_DebeRetornarPromocion() {
        // Arrange
        Long id = 1L;
        Promocion promocion = new Promocion();
        promocion.setId(id);
        promocion.setProductos(List.of());

        when(promocionRepository.findById(id)).thenReturn(Optional.of(promocion));

        // Act
        PromocionDTO resultado = promocionService.obtenerPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(promocionRepository).findById(id);
    }

    @Test
    void obtenerPorId_CuandoNoExisteId_DebeLanzarExcepcion() {
        // Arrange
        Long id = 1L;
        when(promocionRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PromocionException.class, () -> promocionService.obtenerPorId(id));
        verify(promocionRepository).findById(id);
    }

    @Test
    void crear_DebeCrearPromocion() {
        // Arrange
        PromocionDTO dto = new PromocionDTO();
        dto.setNombre("Test Promocion");
        dto.setCodigoPromocion("TEST001");
        dto.setFechaInicio(LocalDate.now());
        dto.setFechaFin(LocalDate.now().plusDays(30));
        dto.setPorcentajeDescuento(10.0);
        dto.setProductosIds(List.of(1L));

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setPrecio(100.0);

        Promocion promocionGuardada = new Promocion();
        promocionGuardada.setId(1L);
        promocionGuardada.setNombre("Test Promocion");
        promocionGuardada.setCodigoPromocion("TEST001");
        promocionGuardada.setFechaInicio(dto.getFechaInicio());
        promocionGuardada.setFechaFin(dto.getFechaFin());
        promocionGuardada.setPorcentajeDescuento(10.0);
        promocionGuardada.setProductos(List.of(producto));

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(promocionRepository.save(any(Promocion.class))).thenReturn(promocionGuardada);

        // Act
        PromocionDTO resultado = promocionService.crear(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Test Promocion", resultado.getNombre());
        assertEquals("TEST001", resultado.getCodigoPromocion());
        assertEquals(10.0, resultado.getPorcentajeDescuento());
        verify(promocionRepository).save(any(Promocion.class));
    }

    @Test
    void actualizar_CuandoExistePromocion_DebeActualizarPromocion() {
        // Arrange
        Long id = 1L;
        PromocionDTO dto = new PromocionDTO();
        dto.setNombre("Promocion Actualizada");
        dto.setCodigoPromocion("ACT001");
        dto.setFechaInicio(LocalDate.now());
        dto.setFechaFin(LocalDate.now().plusDays(30));
        dto.setPorcentajeDescuento(15.0);
        dto.setProductosIds(List.of(1L));

        Promocion promocionExistente = new Promocion();
        promocionExistente.setId(id);
        promocionExistente.setNombre("Promocion Antigua");
        promocionExistente.setCodigoPromocion("OLD001");

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setPrecio(100.0);

        when(promocionRepository.findById(id)).thenReturn(Optional.of(promocionExistente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(promocionRepository.save(any(Promocion.class))).thenReturn(promocionExistente);

        // Act
        PromocionDTO resultado = promocionService.actualizar(id, dto);

        // Assert
        assertNotNull(resultado);
        verify(promocionRepository).save(any(Promocion.class));
        verify(promocionRepository).findById(id);
        verify(productoRepository).findById(1L);
    }

    @Test
    void eliminar_CuandoExistePromocion_DebeEliminarPromocion() {
        // Arrange
        Long id = 1L;
        when(promocionRepository.existsById(id)).thenReturn(true);

        // Act
        promocionService.eliminar(id);

        // Assert
        verify(promocionRepository).deleteById(id);
    }

    @Test
    void eliminar_CuandoNoExistePromocion_DebeLanzarExcepcion() {
        // Arrange
        Long id = 1L;
        when(promocionRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(PromocionException.class, () -> promocionService.eliminar(id));
        verify(promocionRepository, never()).deleteById(any());
    }
}