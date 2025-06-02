package com.example.p_backendsigmaorder.CarritoTest;

import com.example.p_backendsigmaorder.Carrito.Exception.CarritoNotFoundException;
import com.example.p_backendsigmaorder.Carrito.domain.CarritoService;
import com.example.p_backendsigmaorder.Carrito.repository.CarritoRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarritoNotFoundExceptionTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CarritoService carritoService;

    @BeforeEach
    void setUp() {
        // Configurar SecurityContext con un usuario de prueba
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("test@test.com", null)
        );
    }

    @Test
    void deberiaLanzarExcepcionConMensajeCorrecto() {
        // Arrange
        String mensajeEsperado = "Carrito no encontrado para el ID: 1";
        CarritoNotFoundException exception = new CarritoNotFoundException(mensajeEsperado);

        // Act & Assert
        assertEquals(mensajeEsperado, exception.getMessage());
    }

    @Test
    void deberiaPreservarStackTrace() {
        // Arrange
        String mensaje = "Carrito no encontrado";
        CarritoNotFoundException exception = new CarritoNotFoundException(mensaje);
        
        // Act
        try {
            throw exception;
        } catch (CarritoNotFoundException e) {
            // Assert
            assertNotNull(e.getStackTrace());
            assertTrue(e.getStackTrace().length > 0);
        }
    }

    @Test
    void deberiaSerRuntimeException() {
        // Arrange & Act
        CarritoNotFoundException exception = new CarritoNotFoundException("Test");

        // Assert
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void deberiaPermitirMensajeNulo() {
        // Act & Assert
        assertDoesNotThrow(() -> new CarritoNotFoundException(null));
    }

    @Test
    void deberiaMantenerCausaOriginal() {
        // Arrange
        Exception causaOriginal = new IllegalArgumentException("Causa original");
        
        // Act
        CarritoNotFoundException exception = new CarritoNotFoundException("Mensaje");
        exception.initCause(causaOriginal);

        // Assert
        assertEquals(causaOriginal, exception.getCause());
    }

    @Test
    void deberiaFuncionarConMensajeVacio() {
        // Arrange
        String mensajeVacio = "";
        
        // Act
        CarritoNotFoundException exception = new CarritoNotFoundException(mensajeVacio);

        // Assert
        assertEquals(mensajeVacio, exception.getMessage());
    }
}