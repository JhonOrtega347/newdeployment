package com.example.p_backendsigmaorder.Resena.domain;

import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.repository.ProductoRepository;
import com.example.p_backendsigmaorder.Resena.Dto.ResenaRequest;
import com.example.p_backendsigmaorder.Resena.Dto.ResenaResponse;
import com.example.p_backendsigmaorder.Resena.Exception.ResenaAlreadyExistsException;
import com.example.p_backendsigmaorder.Resena.Exception.ResenaNotFoundException;
import com.example.p_backendsigmaorder.Resena.repository.ResenaRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ResenaService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ResenaRepository resenaRepository;

    public List<ResenaResponse> getResenasByProductoId(Long productoId) {
        List<Resena> resenas = resenaRepository.findByProductoId(productoId);
        return resenas.stream().map(this::toResponse).toList();
    }
    private ResenaResponse toResponse(Resena resena) {
        return new ResenaResponse(resena.getProducto().getId(),
                resena.getUsuario().getId(),
                resena.getCalificacion(),
                resena.getComentario(),
                resena.getFecha());
    };
    // --- CLIENTE ---

    public ResenaResponse crearResenaCliente(ResenaRequest request, Long usuarioId) {
        if (resenaRepository.existsByUsuarioIdAndProductoId(usuarioId, request.getProductoId())) {
            throw new ResenaAlreadyExistsException("Ya dejaste una reseña para este producto.");
        }

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResenaNotFoundException("Producto no encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResenaNotFoundException("Usuario no encontrado"));

        Resena nueva = new Resena();
        nueva.setProducto(producto);
        nueva.setUsuario(usuario);
        nueva.setCalificacion(request.getCalificacion());
        nueva.setComentario(request.getComentario());
        nueva.setFecha(LocalDateTime.now());

        resenaRepository.save(nueva);

        return toResponse(nueva);
    }
    public ResenaResponse updateResenaCliente(Long id, ResenaRequest request, Long usuarioId) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new ResenaNotFoundException("Reseña no encontrada"));

        if (!resena.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para modificar esta reseña");
        }

        resena.setCalificacion(request.getCalificacion());
        resena.setComentario(request.getComentario());
        resenaRepository.save(resena);

        return toResponse(resena);
    }
    public void deleteResenaCliente(Long id, Long usuarioId) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new ResenaNotFoundException("Reseña no encontrada"));

        if (!resena.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta reseña");
        }

        resenaRepository.delete(resena);
    }
    // --- ADMINISTRADOR ---
    public ResenaResponse crearResenaAdmin(ResenaRequest request) {
        if (resenaRepository.existsByUsuarioIdAndProductoId(request.getUsuarioId(), request.getProductoId())) {
            throw new ResenaAlreadyExistsException("Este usuario ya dejó una reseña para este producto.");
        }

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResenaNotFoundException("Producto no encontrado"));
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResenaNotFoundException("Usuario no encontrado"));

        Resena nueva = new Resena();
        nueva.setProducto(producto);
        nueva.setUsuario(usuario);
        nueva.setCalificacion(request.getCalificacion());
        nueva.setComentario(request.getComentario());
        nueva.setFecha(LocalDateTime.now());

        resenaRepository.save(nueva);

        return toResponse(nueva);
    }
    public ResenaResponse updateResenaAdmin(Long id, ResenaRequest request) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new ResenaNotFoundException("Reseña no encontrada"));

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResenaNotFoundException("Producto no encontrado"));
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResenaNotFoundException("Usuario no encontrado"));

        resena.setProducto(producto);
        resena.setUsuario(usuario);
        resena.setCalificacion(request.getCalificacion());
        resena.setComentario(request.getComentario());

        resenaRepository.save(resena);

        return toResponse(resena);
    }
    public void deleteResenaAdmin(Long id) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new ResenaNotFoundException("Reseña no encontrada"));
        resenaRepository.delete(resena);
    }

}
