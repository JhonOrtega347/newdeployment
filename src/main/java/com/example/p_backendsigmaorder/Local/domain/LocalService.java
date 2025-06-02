package com.example.p_backendsigmaorder.Local.domain;

import com.example.p_backendsigmaorder.GlobalError.ResourceNotFoundException;
import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Local.DTO.LocalRequestDTO;
import com.example.p_backendsigmaorder.Local.DTO.LocalResponseDTO;
import com.example.p_backendsigmaorder.Local.mapper.LocalMapper;
import com.example.p_backendsigmaorder.Local.repository.LocalRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.data.projection.EntityProjection.ProjectionType.DTO;

@Service
@RequiredArgsConstructor
public class LocalService {

    private final LocalRepository localRepository;
    private final UsuarioRepository usuarioRepository;

    /* ─────────────────────────────────────────────────────────────
       CREA UN LOCAL
       ───────────────────────────────────────────────────────────── */
    @Transactional
    public LocalResponseDTO crearLocal(LocalRequestDTO dto) {

        // 1. Buscar usuario encargado
        Usuario encargado = usuarioRepository.findById(dto.getEncargadoId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Encargado con id " + dto.getEncargadoId() + " no encontrado"));

        // 2. Verificar / asignar rol ENCARGADO_LOCAL
        if (encargado.getRol() != Rol.ENCARGADO_LOCAL && encargado.getRol() != Rol.ADMINISTRADOR) {
            // Puedes decidir lanzar error o actualizar el rol
            encargado.setRol(Rol.ENCARGADO_LOCAL);
            usuarioRepository.save(encargado);
        }

        // 3. Mapear DTO a entidad, guardar y devolver ResponseDTO
        Local local = LocalMapper.toEntity(dto, encargado);
        Local guardado = localRepository.save(local);

        return LocalMapper.toResponseDTO(guardado);
    }

    /* ─────────────────────────────────────────────────────────────
       OBTENER LOCAL POR ID
       ───────────────────────────────────────────────────────────── */
    public Optional<LocalResponseDTO> obtenerPorId(Long id) {
        return localRepository.findById(id)
                .map(LocalMapper::toResponseDTO);
    }

    /* (Opcional) Listar todos los locales */
    public Iterable<LocalResponseDTO> listarTodos() {
        return localRepository.findAll()
                .stream()
                .map(LocalMapper::toResponseDTO)
                .toList();
    }




}

