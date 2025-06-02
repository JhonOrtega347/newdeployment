package com.example.p_backendsigmaorder.Promocion.Controller;

import com.example.p_backendsigmaorder.Promocion.Service.PromocionService;
import com.example.p_backendsigmaorder.Promocion.dto.PromocionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promociones")
public class PromocionController {

    private final PromocionService promocionService;

    @Autowired
    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @GetMapping
    public List<PromocionDTO> listar() {
        return promocionService.listar();
    }

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @GetMapping("/{id}")
    public PromocionDTO obtener(@PathVariable Long id) {
        return promocionService.obtenerPorId(id);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @PostMapping
    public PromocionDTO crear(@RequestBody PromocionDTO dto) {
        return promocionService.crear(dto);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @PutMapping("/{id}")
    public PromocionDTO actualizar(@PathVariable Long id, @RequestBody PromocionDTO dto) {
        return promocionService.actualizar(id, dto);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ENCARGADO_LOCAL')")
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        promocionService.eliminar(id);
    }
}
