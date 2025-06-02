package com.example.p_backendsigmaorder.ResenaTest;

import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import com.example.p_backendsigmaorder.Resena.Dto.ResenaRequest;
import com.example.p_backendsigmaorder.Resena.Dto.ResenaResponse;
import com.example.p_backendsigmaorder.Resena.Exception.ResenaAlreadyExistsException;
import com.example.p_backendsigmaorder.Resena.Exception.ResenaNotFoundException;
import com.example.p_backendsigmaorder.Resena.domain.Resena;
import com.example.p_backendsigmaorder.Resena.domain.ResenaService;
import com.example.p_backendsigmaorder.Resena.repository.ResenaRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ResenaService resenaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getResenasByProductoId_DeberiaRetornarListaDeResenas() {
        // Arrange
        Long productoId = 1L;
        Producto producto = new Producto();
        producto.setId(productoId);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Resena resena = new Resena();
        resena.setProducto(producto);
        resena.setUsuario(usuario);
        resena.setCalificacion(5);
        resena.setComentario("Excelente producto");
        resena.setFecha(LocalDateTime.now());

        when(resenaRepository.findByProductoId(productoId)).thenReturn(Arrays.asList(resena));

        // Act
        List<ResenaResponse> resultado = resenaService.getResenasByProductoId(productoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getCalificacion());
        assertEquals("Excelente producto", resultado.get(0).getComentario());
    }

    @Test
    void crearResenaCliente_DeberiaCrearResenaExitosamente() {
        // Arrange
        Long usuarioId = 1L;
        Long productoId = 1L;
        ResenaRequest request = new ResenaRequest();
        request.setProductoId(productoId);
        request.setCalificacion(5);
        request.setComentario("Excelente producto");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Producto producto = new Producto();
        producto.setId(productoId);

        when(resenaRepository.existsByUsuarioIdAndProductoId(usuarioId, productoId)).thenReturn(false);
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(resenaRepository.save(any(Resena.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        ResenaResponse resultado = resenaService.crearResenaCliente(request, usuarioId);

        // Assert
        assertNotNull(resultado);
        assertEquals(productoId, resultado.getProductoId());
        assertEquals(usuarioId, resultado.getUsuarioId());
        assertEquals(5, resultado.getCalificacion());
    }

    @Test
    void crearResenaCliente_DeberiaLanzarExcepcionSiYaExisteResena() {
        // Arrange
        Long usuarioId = 1L;
        Long productoId = 1L;
        ResenaRequest request = new ResenaRequest();
        request.setProductoId(productoId);

        when(resenaRepository.existsByUsuarioIdAndProductoId(usuarioId, productoId)).thenReturn(true);

        // Act & Assert
        assertThrows(ResenaAlreadyExistsException.class, () -> {
            resenaService.crearResenaCliente(request, usuarioId);
        });
    }

    @Test
    void updateResenaCliente_DeberiaActualizarResenaExitosamente() {
        // Arrange
        Long resenaId = 1L;
        Long usuarioId = 1L;
        Long productoId = 1L;
        ResenaRequest request = new ResenaRequest();
        request.setCalificacion(4);
        request.setComentario("Actualización del comentario");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Producto producto = new Producto();
        producto.setId(productoId);

        Resena resenaExistente = new Resena();
        resenaExistente.setId(resenaId);
        resenaExistente.setUsuario(usuario);
        resenaExistente.setProducto(producto);

        when(resenaRepository.findById(resenaId)).thenReturn(Optional.of(resenaExistente));
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaExistente);

        // Act
        ResenaResponse resultado = resenaService.updateResenaCliente(resenaId, request, usuarioId);

        // Assert
        assertNotNull(resultado);
        assertEquals(4, resultado.getCalificacion());
        assertEquals("Actualización del comentario", resultado.getComentario());
        verify(resenaRepository).save(any(Resena.class));
    }

    @Test
    void deleteResenaCliente_DeberiaEliminarResenaExitosamente() {
        // Arrange
        Long resenaId = 1L;
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Resena resena = new Resena();
        resena.setId(resenaId);
        resena.setUsuario(usuario);

        when(resenaRepository.findById(resenaId)).thenReturn(Optional.of(resena));

        // Act
        resenaService.deleteResenaCliente(resenaId, usuarioId);

        // Assert
        verify(resenaRepository).delete(resena);
    }

    @Test
    void deleteResenaCliente_DeberiaLanzarExcepcionSiNoExisteResena() {
        // Arrange
        Long resenaId = 1L;
        Long usuarioId = 1L;

        when(resenaRepository.findById(resenaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResenaNotFoundException.class, () -> {
            resenaService.deleteResenaCliente(resenaId, usuarioId);
        });
    }
}