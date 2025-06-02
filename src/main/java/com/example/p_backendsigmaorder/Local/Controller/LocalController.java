package com.example.p_backendsigmaorder.Local.Controller;

import com.example.p_backendsigmaorder.Local.DTO.LocalRequestDTO;
import com.example.p_backendsigmaorder.Local.DTO.LocalResponseDTO;
import com.example.p_backendsigmaorder.Local.domain.LocalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locales")
@RequiredArgsConstructor
public class LocalController {

    private final LocalService localService;

    /* ──────────────────────────────────────────────
       POST /api/locales
       Crea un nuevo local
       ────────────────────────────────────────────── */
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<LocalResponseDTO> crearLocal(
            @Valid @RequestBody LocalRequestDTO request) {

        LocalResponseDTO response = localService.crearLocal(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /* ──────────────────────────────────────────────
       GET /api/locales/{id}
       Obtiene un local por ID
       ────────────────────────────────────────────── */
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<LocalResponseDTO> obtenerPorId(@PathVariable Long id) {
        return localService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ──────────────────────────────────────────────
       (Opcional) GET /api/locales
       Lista todos los locales
       ────────────────────────────────────────────── */
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<Iterable<LocalResponseDTO>> listarTodos() {
        return ResponseEntity.ok(localService.listarTodos());
    }
}
