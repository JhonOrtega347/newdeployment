package com.example.p_backendsigmaorder.Usuario.Controller;

import com.example.p_backendsigmaorder.Security.service.AuthenticationService;
import com.example.p_backendsigmaorder.Usuario.DTO.ChangePasswordRequest;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.domain.UsuarioService;
import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final AuthenticationService authenticationService;
    private final UsuarioService usuarioService;
/*
    // ────────────────────────────
    // POST /api/usuarios
    // Crea un nuevo usuario
    // ────────────────────────────
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(
            @Valid @RequestBody UsuarioRequestDTO request) {

        UsuarioResponseDTO response = usuarioService.registrarUsuario(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
*/
    // ────────────────────────────
    // GET /api/usuarios/{id}
    // Obtiene usuario por ID
    // ────────────────────────────



    //yala
    //administrador
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    //yala
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR')")
    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser() {
        try {
            Usuario authenticatedUser = authenticationService.getAuthenticatedUser();
            return ResponseEntity.ok(authenticatedUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }
    }

//nola
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR')")
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        authenticationService.changePassword(correo, request);

        return ResponseEntity.noContent().build();
    }
}
