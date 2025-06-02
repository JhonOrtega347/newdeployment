package com.example.p_backendsigmaorder.Pedido.Controller;
import com.example.p_backendsigmaorder.Pedido.domain.PedidoService;
import com.example.p_backendsigmaorder.Pedido.DTO.PedidoRequestDTO;
import com.example.p_backendsigmaorder.Pedido.DTO.PedidoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido() {
        return ResponseEntity.ok(pedidoService.crearDesdeCarrito());
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping
    public ResponseEntity<?> listarPedidos() {
        // implementa findByUsuario...
        return ResponseEntity.ok().build();
    }
}
