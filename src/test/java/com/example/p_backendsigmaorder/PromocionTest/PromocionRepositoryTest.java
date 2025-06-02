package com.example.p_backendsigmaorder.PromocionTest;

import com.example.p_backendsigmaorder.Promocion.Repository.PromocionRepository;
import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PromocionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PromocionRepository promocionRepository;

    @Test
    void findByFechaInicioBeforeAndFechaFinAfter_deberiaEncontrarPromocionesActivas() {
        // Arrange
        LocalDate fechaActual = LocalDate.now();

        // Crear promoción activa
        Promocion promocionActiva = new Promocion();
        promocionActiva.setCodigoPromocion("PROMO1");
        promocionActiva.setNombre("Promoción Activa");
        promocionActiva.setDescripcion("Promoción de prueba activa");
        promocionActiva.setFechaInicio(fechaActual.minusDays(5));
        promocionActiva.setFechaFin(fechaActual.plusDays(5));
        promocionActiva.setPrecioOriginal(100.0);
        promocionActiva.setPorcentajeDescuento(20.0);

        // Crear promoción expirada
        Promocion promocionExpirada = new Promocion();
        promocionExpirada.setCodigoPromocion("PROMO2");
        promocionExpirada.setNombre("Promoción Expirada");
        promocionExpirada.setDescripcion("Promoción de prueba expirada");
        promocionExpirada.setFechaInicio(fechaActual.minusDays(10));
        promocionExpirada.setFechaFin(fechaActual.minusDays(5));
        promocionExpirada.setPrecioOriginal(200.0);
        promocionExpirada.setPorcentajeDescuento(30.0);

        // Persistir las promociones
        entityManager.persist(promocionActiva);
        entityManager.persist(promocionExpirada);
        entityManager.flush();

        // Act
        List<Promocion> promocionesActivas = promocionRepository
                .findByFechaInicioBeforeAndFechaFinAfter(fechaActual, fechaActual);

        // Assert
        assertNotNull(promocionesActivas);
        assertEquals(1, promocionesActivas.size());
        assertEquals("PROMO1", promocionesActivas.get(0).getCodigoPromocion());
    }

    @Test
    void findByFechaInicioBeforeAndFechaFinAfter_cuandoNoHayPromociones_deberiaRetornarListaVacia() {
        // Arrange
        LocalDate fechaActual = LocalDate.now();

        // Act
        List<Promocion> promociones = promocionRepository
                .findByFechaInicioBeforeAndFechaFinAfter(fechaActual, fechaActual);

        // Assert
        assertNotNull(promociones);
        assertTrue(promociones.isEmpty());
    }

    @Test
    void findByFechaInicioBeforeAndFechaFinAfter_deberiaEncontrarPromocionesEnRangoDeFechas() {
        // Arrange
        LocalDate inicio = LocalDate.now().plusDays(5);
        LocalDate fin = LocalDate.now().plusDays(15);

        Promocion promocionFutura = new Promocion();
        promocionFutura.setCodigoPromocion("PROMO3");
        promocionFutura.setNombre("Promoción Futura");
        promocionFutura.setDescripcion("Promoción de prueba futura");
        promocionFutura.setFechaInicio(LocalDate.now().plusDays(10));
        promocionFutura.setFechaFin(LocalDate.now().plusDays(20));
        promocionFutura.setPrecioOriginal(150.0);
        promocionFutura.setPorcentajeDescuento(25.0);

        entityManager.persist(promocionFutura);
        entityManager.flush();

        // Act
        List<Promocion> promocionesEnRango = promocionRepository
                .findByFechaInicioBeforeAndFechaFinAfter(fin, inicio);

        // Assert
        assertNotNull(promocionesEnRango);
        assertEquals(1, promocionesEnRango.size());
        assertEquals("PROMO3", promocionesEnRango.get(0).getCodigoPromocion());
    }
}