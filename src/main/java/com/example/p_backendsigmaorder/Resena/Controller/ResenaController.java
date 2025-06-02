package com.example.p_backendsigmaorder.Resena.Controller;

import com.example.p_backendsigmaorder.Resena.Dto.ResenaRequest;
import com.example.p_backendsigmaorder.Resena.Dto.ResenaResponse;
import com.example.p_backendsigmaorder.Resena.domain.ResenaService;
import com.example.p_backendsigmaorder.Security.service.AuthenticationService;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private final ResenaService resenaService;
    private final AuthenticationService authenticationService;

    public ResenaController(ResenaService resenaService, AuthenticationService authenticationService) {
        this.resenaService = resenaService;
        this.authenticationService = authenticationService;
    }

    // --- ENDPOINTS PARA CLIENTE ---

    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMINISTRADOR')")
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<ResenaResponse>> getResenasByProductoId(@PathVariable Long idProducto) {
        List<ResenaResponse> resenas = resenaService.getResenasByProductoId(idProducto);
        return ResponseEntity.ok(resenas);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/mis-resenas")
    public ResponseEntity<List<ResenaResponse>> getMisResenas() {
        Usuario usuario = authenticationService.getAuthenticatedUser();
        List<ResenaResponse> resenas = resenaService.getResenasByProductoId(usuario.getId());
        return ResponseEntity.ok(resenas);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping
    public ResponseEntity<ResenaResponse> createResenaCliente(@RequestBody ResenaRequest request) {
        Usuario usuario = authenticationService.getAuthenticatedUser();
        ResenaResponse resenaResponse = resenaService.crearResenaCliente(request, usuario.getId());
        return ResponseEntity.ok(resenaResponse);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PutMapping("/{id_resena}")
    public ResponseEntity<ResenaResponse> updateResenaCliente(@PathVariable Long id_resena,
                                                              @RequestBody ResenaRequest request) {
        Usuario usuario = authenticationService.getAuthenticatedUser();
        ResenaResponse updated = resenaService.updateResenaCliente(id_resena, request, usuario.getId());
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @DeleteMapping("/{id_resena}")
    public ResponseEntity<Void> deleteResenaCliente(@PathVariable Long id_resena) {
        Usuario usuario = authenticationService.getAuthenticatedUser();
        resenaService.deleteResenaCliente(id_resena, usuario.getId());
        return ResponseEntity.noContent().build();
    }


    // --- ENDPOINTS PARA ADMINISTRADOR ---

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ResenaResponse>> getResenasByUsuarioId(@PathVariable Long idUsuario) {
        List<ResenaResponse> resenas = resenaService.getResenasByProductoId(idUsuario);
        return ResponseEntity.ok(resenas);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/admin")
    public ResponseEntity<ResenaResponse> createResenaAdmin(@RequestBody ResenaRequest request) {
        ResenaResponse resenaResponse = resenaService.crearResenaAdmin(request);
        return ResponseEntity.ok(resenaResponse);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/admin/{id_resena}")
    public ResponseEntity<ResenaResponse> updateResenaAdmin(@PathVariable Long id_resena,
                                                            @RequestBody ResenaRequest request) {
        ResenaResponse updated = resenaService.updateResenaAdmin(id_resena, request);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/admin/{id_resena}")
    public ResponseEntity<Void> deleteResenaAdmin(@PathVariable Long id_resena) {
        resenaService.deleteResenaAdmin(id_resena);
        return ResponseEntity.noContent().build();
    }
}
