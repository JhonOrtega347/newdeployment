package com.example.p_backendsigmaorder.UsuarioTest;

import com.example.p_backendsigmaorder.Security.service.AuthenticationService;
import com.example.p_backendsigmaorder.Usuario.Controller.UsuarioController;
import com.example.p_backendsigmaorder.Usuario.DTO.ChangePasswordRequest;
import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioResponseDTO;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.domain.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioResponseDTO usuarioResponseDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        // Inicializar un DTO de ejemplo
        usuarioResponseDTO = UsuarioResponseDTO.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .correo("juan.perez@ejemplo.com")
                .direccion("Av. Principal 123")
                .telefono("123456789")
                .fechaRegistro(new Date())
                .rol(Rol.CLIENTE)
                .build();

        // Inicializar un Usuario de ejemplo
        usuario = new Usuario();
        // Aquí deberías establecer los campos necesarios del Usuario
        // dependiendo de su implementación
    }

    @Test
    void obtenerPorId_cuandoExisteUsuario_retornaUsuario() {
        // Arrange
        Long id = 1L;
        when(usuarioService.obtenerPorId(id)).thenReturn(Optional.of(usuarioResponseDTO));

        // Act
        ResponseEntity<UsuarioResponseDTO> response = usuarioController.obtenerPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(usuarioResponseDTO.getId(), response.getBody().getId());
        assertEquals(usuarioResponseDTO.getNombre(), response.getBody().getNombre());
        assertEquals(usuarioResponseDTO.getCorreo(), response.getBody().getCorreo());
        assertEquals(usuarioResponseDTO.getDireccion(), response.getBody().getDireccion());
        assertEquals(usuarioResponseDTO.getTelefono(), response.getBody().getTelefono());
        assertEquals(usuarioResponseDTO.getRol(), response.getBody().getRol());
        verify(usuarioService).obtenerPorId(id);
    }

    @Test
    void obtenerPorId_cuandoNoExisteUsuario_retornaNotFound() {
        // Arrange
        Long id = 1L;
        when(usuarioService.obtenerPorId(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UsuarioResponseDTO> response = usuarioController.obtenerPorId(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(usuarioService).obtenerPorId(id);
    }

    @Test
    void getAuthenticatedUser_cuandoUsuarioAutenticado_retornaUsuario() {
        // Arrange
        when(authenticationService.getAuthenticatedUser()).thenReturn(usuario);

        // Act
        ResponseEntity<?> response = usuarioController.getAuthenticatedUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Usuario);
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void getAuthenticatedUser_cuandoNoHayUsuarioAutenticado_retornaUnauthorized() {
        // Arrange
        when(authenticationService.getAuthenticatedUser())
                .thenThrow(new RuntimeException("Usuario no autenticado"));

        // Act
        ResponseEntity<?> response = usuarioController.getAuthenticatedUser();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Usuario no autenticado", response.getBody());
        verify(authenticationService).getAuthenticatedUser();
    }

    @Test
    void changePassword_retornaNoContent() {
        // Arrange
        ChangePasswordRequest request = new ChangePasswordRequest();
        String correo = "juan.perez@ejemplo.com";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(correo);
        doNothing().when(authenticationService).changePassword(correo, request);

        // Act
        ResponseEntity<Void> response = usuarioController.changePassword(request);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(authenticationService).changePassword(correo, request);
        verify(authentication).getName();
        verify(securityContext).getAuthentication();
    }
}