package com.example.p_backendsigmaorder.Pedido.repository;

import com.example.p_backendsigmaorder.Pedido.domain.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}
