package com.example.p_backendsigmaorder.Resena.repository;

import com.example.p_backendsigmaorder.Resena.domain.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Por ejemplo, para buscar reseñas por producto o usuario
    List<Resena> findByProductoId(Long productoId);
    boolean existsByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

}
