package com.example.p_backendsigmaorder.Carrito.Controller;

import com.example.p_backendsigmaorder.Carrito.DTO.*;
import com.example.p_backendsigmaorder.Carrito.domain.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @GetMapping
    public CarritoResponseDTO obtenerCarrito() {
        return carritoService.obtenerCarritoDTO();
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @PostMapping("/producto/{productoId}")
    public void agregarProducto(@PathVariable Long productoId, @RequestParam int cantidad) {
        carritoService.agregarProducto(productoId, cantidad);
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @DeleteMapping("/producto/{productoId}")
    public void eliminarProducto(@PathVariable Long productoId) {
        carritoService.removerProducto(productoId);
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @PostMapping("/promocion/{promocionId}")
    public void agregarPromocion(@PathVariable Long promocionId, @RequestParam int cantidad) {
        carritoService.agregarPromocion(promocionId, cantidad);
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @DeleteMapping("/promocion/{promocionId}")
    public void eliminarPromocion(@PathVariable Long promocionId) {
        carritoService.removerPromocion(promocionId);
    }


    // Actualizar método de envío (PICKUP o DELIVERY)
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @PostMapping("/metodo-envio")
    public ResponseEntity<?> setMetodoEnvio(@Valid @RequestBody MetodoEnvioDTO dto) {
        try {
            CarritoResponseDTO actualizado = carritoService.actualizarMetodoEnvio(dto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}