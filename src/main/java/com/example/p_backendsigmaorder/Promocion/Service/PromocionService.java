package com.example.p_backendsigmaorder.Promocion.Service;

import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import com.example.p_backendsigmaorder.Promocion.dto.PromocionDTO;
import java.util.List;

public interface PromocionService {
    List<PromocionDTO> listar();
    PromocionDTO obtenerPorId(Long id);
    PromocionDTO crear(PromocionDTO dto);
    PromocionDTO actualizar(Long id, PromocionDTO dto);
    void eliminar(Long id);
    List<Promocion> findActive();
}