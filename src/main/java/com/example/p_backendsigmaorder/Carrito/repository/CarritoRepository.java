package com.example.p_backendsigmaorder.Carrito.repository;

import com.example.p_backendsigmaorder.Carrito.domain.Carrito;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuario(Usuario usuario);
}