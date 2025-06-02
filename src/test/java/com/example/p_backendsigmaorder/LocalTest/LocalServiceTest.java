package com.example.p_backendsigmaorder.LocalTest;

import com.example.p_backendsigmaorder.Local.DTO.LocalRequestDTO;
import com.example.p_backendsigmaorder.Local.DTO.LocalResponseDTO;
import com.example.p_backendsigmaorder.Local.domain.Local;
import com.example.p_backendsigmaorder.Local.domain.LocalService;
import com.example.p_backendsigmaorder.Local.repository.LocalRepository;
import com.example.p_backendsigmaorder.Usuario.domain.Rol;
import com.example.p_backendsigmaorder.Usuario.domain.Usuario;
import com.example.p_backendsigmaorder.Usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocalServiceTest {

    @Mock
    private LocalRepository localRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private LocalService localService;

    @Test
    void testCrearLocal() {
        LocalRequestDTO requestDTO = new LocalRequestDTO();
        requestDTO.setSede("Sede Central");
        requestDTO.setDireccion("Av. Principal 123");
        requestDTO.setTelefono("+51987654321");
        requestDTO.setHorario("09:00–18:00");
        requestDTO.setEncargadoId(1L);

        Usuario encargado = new Usuario();
        encargado.setId(1L);
        encargado.setNombre("Juan Pérez");
        encargado.setRol(Rol.ENCARGADO_LOCAL);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(encargado));
        when(localRepository.save(any(Local.class))).thenAnswer(invocation -> {
            Local local = invocation.getArgument(0);
            local.setId(1L);
            return local;
        });

        LocalResponseDTO responseDTO = localService.crearLocal(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Sede Central", responseDTO.getSede());
        assertEquals("Juan Pérez", responseDTO.getEncargado().getNombre());
    }

    @Test
    void testObtenerPorId() {
        Long localId = 1L;
        Usuario encargado = new Usuario();
        encargado.setId(1L);
        encargado.setNombre("Juan Pérez");

        Local local = new Local();
        local.setId(localId);
        local.setSede("Sede Central");
        local.setDireccion("Av. Principal 123");
        local.setTelefono("+51987654321");
        local.setHorario("09:00–18:00");
        local.setEncargado(encargado);

        when(localRepository.findById(localId)).thenReturn(Optional.of(local));

        Optional<LocalResponseDTO> response = localService.obtenerPorId(localId);

        assertTrue(response.isPresent());
        assertEquals("Sede Central", response.get().getSede());
    }

    @Test
    void testListarTodos() {
        Usuario encargado = new Usuario();
        encargado.setId(1L);
        encargado.setNombre("Juan Pérez");

        Local local = new Local();
        local.setId(1L);
        local.setSede("Sede Central");
        local.setDireccion("Av. Principal 123");
        local.setTelefono("+51987654321");
        local.setHorario("09:00–18:00");
        local.setEncargado(encargado);

        when(localRepository.findAll()).thenReturn(List.of(local));

        List<LocalResponseDTO> locales = (List<LocalResponseDTO>) localService.listarTodos();

        assertFalse(locales.isEmpty());
        assertEquals("Sede Central", locales.get(0).getSede());
    }
}
