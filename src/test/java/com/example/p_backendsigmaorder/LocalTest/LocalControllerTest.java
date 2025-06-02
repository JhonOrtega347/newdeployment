package com.example.p_backendsigmaorder.LocalTest;

import com.example.p_backendsigmaorder.Local.Controller.LocalController;
import com.example.p_backendsigmaorder.Local.DTO.LocalRequestDTO;
import com.example.p_backendsigmaorder.Local.DTO.LocalResponseDTO;
import com.example.p_backendsigmaorder.Local.domain.LocalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LocalControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private LocalService localService;  // Mock tradicional de Mockito

    @InjectMocks
    private LocalController localController;  // Inyecta los mocks en el controlador

    @BeforeEach
    void setup() {
        // Configuración manual de MockMvc sin Spring Boot
        mockMvc = MockMvcBuilders.standaloneSetup(localController).build();
    }

    // VERIFICAR ESTE TEST
    @Test
    void crearLocal_deberiaRetornar201() throws Exception {
        LocalRequestDTO request = new LocalRequestDTO();
        request.setSede("Sede Lima");
        request.setDireccion("Av. Siempre Viva 123");
        request.setTelefono("+51987654321");
        request.setHorario("8am - 5pm");
        request.setEncargadoId(1L);

        LocalResponseDTO response = LocalResponseDTO.builder()
                .id(1L)
                .sede("Sede Lima")
                .build();

        when(localService.crearLocal(request)).thenReturn(response);

        mockMvc.perform(post("/api/locales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sede").value("Sede Lima"));
    }

    @Test
    void obtenerPorId_deberiaRetornar200() throws Exception {
        LocalResponseDTO response = LocalResponseDTO.builder()
                .id(1L)
                .sede("Sede Norte")
                .build();

        when(localService.obtenerPorId(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/locales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.sede", is("Sede Norte")));
    }

    @Test
    void obtenerPorId_noExistente_deberiaRetornar404() throws Exception {
        when(localService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/locales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarTodos_deberiaRetornarLista() throws Exception {
        LocalResponseDTO local = LocalResponseDTO.builder()
                .id(1L)
                .sede("Sede Principal")
                .build();

        when(localService.listarTodos()).thenReturn(Collections.singletonList(local));

        mockMvc.perform(get("/api/locales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sede", is("Sede Principal")));
    }
}