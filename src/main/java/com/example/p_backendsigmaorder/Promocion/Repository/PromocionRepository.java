package com.example.p_backendsigmaorder.Promocion.Repository;

import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {
        List<Promocion> findByFechaInicioBeforeAndFechaFinAfter(LocalDate inicio, LocalDate fin);
    }

