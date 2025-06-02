package com.example.p_backendsigmaorder.Carrito.repository;

import com.example.p_backendsigmaorder.Carrito.domain.CarritoProducto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Long> {
}
