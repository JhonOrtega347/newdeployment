package com.example.p_backendsigmaorder.Pedido.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PedidoNotFoundException extends ResponseStatusException {
    public PedidoNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Pedido con id " + id + " no encontrado");
    }
}
