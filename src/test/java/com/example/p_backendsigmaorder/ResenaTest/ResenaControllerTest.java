package com.example.p_backendsigmaorder.ResenaTest;

import com.example.p_backendsigmaorder.Resena.Controller.ResenaController;
import com.example.p_backendsigmaorder.Resena.Dto.ResenaRequest;
import com.example.p_backendsigmaorder.Resena.Dto.ResenaResponse;
import com.example.p_backendsigmaorder.Resena.domain.ResenaService;
import com.example.p_backendsigmaorder.Security.service.AuthenticationService;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ResenaControllerTest {

    @Mock
    private ResenaService resenaService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private ResenaController resenaController;

    private Usuario usuarioMock;
    private ResenaResponse resenaResponseMock;
    private ResenaRequest resenaRequestMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración del usuario mock
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);

        // Configuración de la reseña response mock
        resenaResponseMock = new ResenaResponse(1L, 1L, 5, "Excelente producto", LocalDateTime.now());

        // Configuración de la reseña request mock
        resenaRequestMock = new ResenaRequest();
        resenaRequestMock.setProductoId(1L);
        resenaRequestMock.setCalificacion(5);
        resenaRequestMock.setComentario("Excelente producto");
    }

    @Test
    void getResenasByProductoId_DeberiaRetornarResenas() {
        // Arrange
        Long productoId = 1L;
        List<ResenaResponse> resenasMock = Arrays.asList(
                new ResenaResponse(1L, 1L, 5, "Excelente producto", LocalDateTime.now()),
                new ResenaResponse(1L, 2L, 4, "Buen producto", LocalDateTime.now())
        );
        when(resenaService.getResenasByProductoId(productoId)).thenReturn(resenasMock);

        // Act
        ResponseEntity<List<ResenaResponse>> response = resenaController.getResenasByProductoId(productoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(resenaService).getResenasByProductoId(productoId);
    }

    @Test
    void getMisResenas_DeberiaRetornarResenasDelUsuario() {
        // Arrange
        List<ResenaResponse> resenasMock = Arrays.asList(
                new ResenaResponse(1L, 1L, 5, "Excelente producto", LocalDateTime.now()),
                new ResenaResponse(2L, 1L, 4, "Buen producto", LocalDateTime.now())
        );
        when(authenticationService.getAuthenticatedUser()).thenReturn(usuarioMock);
        when(resenaService.getResenasByProductoId(usuarioMock.getId())).thenReturn(resenasMock);

        // Act
        ResponseEntity<List<ResenaResponse>> response = resenaController.getMisResenas();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(authenticationService).getAuthenticatedUser();
        verify(resenaService).getResenasByProductoId(usuarioMock.getId());
    }

    @Test
    void createResenaCliente_DeberiaCrearResena() {
        // Arrange
        when(authenticationService.getAuthenticatedUser()).thenReturn(usuarioMock);
        when(resenaService.crearResenaCliente(any(ResenaRequest.class), anyLong()))
                .thenReturn(resenaResponseMock);

        // Act
        ResponseEntity<ResenaResponse> response = resenaController.createResenaCliente(resenaRequestMock);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(authenticationService).getAuthenticatedUser();
        verify(resenaService).crearResenaCliente(any(ResenaRequest.class), eq(usuarioMock.getId()));
    }

    @Test
    void updateResenaCliente_DeberiaActualizarResena() {
        // Arrange
        Long resenaId = 1L;
        when(authenticationService.getAuthenticatedUser()).thenReturn(usuarioMock);
        when(resenaService.updateResenaCliente(anyLong(), any(ResenaRequest.class), anyLong()))
                .thenReturn(resenaResponseMock);

        // Act
        ResponseEntity<ResenaResponse> response = resenaController.updateResenaCliente(resenaId, resenaRequestMock);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(authenticationService).getAuthenticatedUser();
        verify(resenaService).updateResenaCliente(eq(resenaId), any(ResenaRequest.class), eq(usuarioMock.getId()));
    }

    @Test
    void deleteResenaCliente_DeberiaEliminarResena() {
        // Arrange
        Long resenaId = 1L;
        when(authenticationService.getAuthenticatedUser()).thenReturn(usuarioMock);
        doNothing().when(resenaService).deleteResenaCliente(resenaId, usuarioMock.getId());

        // Act
        ResponseEntity<Void> response = resenaController.deleteResenaCliente(resenaId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authenticationService).getAuthenticatedUser();
        verify(resenaService).deleteResenaCliente(resenaId, usuarioMock.getId());
    }

    @Test
    void getResenasByUsuarioId_DeberiaRetornarResenas() {
        // Arrange
        Long usuarioId = 1L;
        List<ResenaResponse> resenasMock = Arrays.asList(
                new ResenaResponse(1L, usuarioId, 5, "Excelente producto", LocalDateTime.now()),
                new ResenaResponse(2L, usuarioId, 4, "Buen producto", LocalDateTime.now())
        );
        when(resenaService.getResenasByProductoId(usuarioId)).thenReturn(resenasMock);

        // Act
        ResponseEntity<List<ResenaResponse>> response = resenaController.getResenasByUsuarioId(usuarioId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(resenaService).getResenasByProductoId(usuarioId);
    }

    @Test
    void createResenaAdmin_DeberiaCrearResena() {
        // Arrange
        when(resenaService.crearResenaAdmin(any(ResenaRequest.class))).thenReturn(resenaResponseMock);

        // Act
        ResponseEntity<ResenaResponse> response = resenaController.createResenaAdmin(resenaRequestMock);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(resenaService).crearResenaAdmin(resenaRequestMock);
    }

    @Test
    void updateResenaAdmin_DeberiaActualizarResena() {
        // Arrange
        Long resenaId = 1L;
        when(resenaService.updateResenaAdmin(anyLong(), any(ResenaRequest.class)))
                .thenReturn(resenaResponseMock);

        // Act
        ResponseEntity<ResenaResponse> response = resenaController.updateResenaAdmin(resenaId, resenaRequestMock);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(resenaService).updateResenaAdmin(eq(resenaId), any(ResenaRequest.class));
    }

    @Test
    void deleteResenaAdmin_DeberiaEliminarResena() {
        // Arrange
        Long resenaId = 1L;
        doNothing().when(resenaService).deleteResenaAdmin(resenaId);

        // Act
        ResponseEntity<Void> response = resenaController.deleteResenaAdmin(resenaId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(resenaService).deleteResenaAdmin(resenaId);
    }
}