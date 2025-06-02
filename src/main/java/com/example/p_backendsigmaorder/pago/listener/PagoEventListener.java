package com.example.p_backendsigmaorder.pago.listener;

import com.example.p_backendsigmaorder.Pedido.DTO.PedidoResponseDTO;
import com.example.p_backendsigmaorder.Pedido.domain.PedidoService;
import com.example.p_backendsigmaorder.email.EmailService;
import com.example.p_backendsigmaorder.pago.event.CheckoutSessionCompletedEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PagoEventListener {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private EmailService emailService;

    @Async("taskExecutor")
    @EventListener
    public void onCheckoutCompleted(CheckoutSessionCompletedEvent event) {
        // 1) Crear el pedido desde el carrito
        PedidoResponseDTO pedido = pedidoService.crearDesdeCarrito();

        // 2) Enviar email de confirmación
        emailService.enviarConfirmacionPedido(pedido, event.getJwt());
    }
}