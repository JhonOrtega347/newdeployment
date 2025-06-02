package com.example.p_backendsigmaorder.Usuario.domain;

import com.example.p_backendsigmaorder.Carrito.repository.CarritoRepository;
import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioRequestDTO;
import com.example.p_backendsigmaorder.Usuario.DTO.UsuarioResponseDTO;
import com.example.p_backendsigmaorder.Usuario.mapper.UsuarioMapper;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import com.example.p_backendsigmaorder.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final EmailService emailService;

    private final UsuarioRepository usuarioRepository;



    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioMapper::toResponseDTO);
    }

}

/*@Service
@RequiredArgsConstructor
public class UsuarioService {
*/
/*
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;  // inyectar repo carrito

    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        Usuario nuevoUsuario = UsuarioMapper.toEntity(dto);
        nuevoUsuario.setRol(/* asigna rol por defecto, ej: CLIENTE */
/*
Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

// Crear carrito asociado al usuario
Carrito carrito = new Carrito();
        carrito.setUsuario(usuarioGuardado);
        carritoRepository.save(carrito);

        return UsuarioMapper.toResponseDTO(usuarioGuardado);
    }

public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
    return usuarioRepository.findById(id)
            .map(UsuarioMapper::toResponseDTO);
}
}
*/