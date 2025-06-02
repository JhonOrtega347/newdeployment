package com.example.p_backendsigmaorder.PromocionTest;

import com.example.p_backendsigmaorder.Promocion.Controller.PromocionController;
import com.example.p_backendsigmaorder.Promocion.Service.PromocionService;
import com.example.p_backendsigmaorder.Promocion.dto.PromocionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PromocionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PromocionService promocionService;

    @InjectMocks
    private PromocionController promocionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(promocionController).build();
    }

    @Test
    void listar() throws Exception {
        PromocionDTO dto1 = new PromocionDTO();
        PromocionDTO dto2 = new PromocionDTO();
        List<PromocionDTO> promociones = Arrays.asList(dto1, dto2);

        when(promocionService.listar()).thenReturn(promociones);

        mockMvc.perform(get("/api/promociones"))
                .andExpect(status().isOk());

        verify(promocionService).listar();
    }

    @Test
    void obtener() throws Exception {
        Long id = 1L;
        PromocionDTO dto = new PromocionDTO();
        when(promocionService.obtenerPorId(id)).thenReturn(dto);

        mockMvc.perform(get("/api/promociones/{id}", id))
                .andExpect(status().isOk());

        verify(promocionService).obtenerPorId(id);
    }

    @Test
    void crear() throws Exception {
        PromocionDTO dto = new PromocionDTO();
        when(promocionService.crear(any(PromocionDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/promociones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        verify(promocionService).crear(any(PromocionDTO.class));
    }

    @Test
    void actualizar() throws Exception {
        Long id = 1L;
        PromocionDTO dto = new PromocionDTO();
        when(promocionService.actualizar(eq(id), any(PromocionDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/promociones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        verify(promocionService).actualizar(eq(id), any(PromocionDTO.class));
    }

    @Test
    void eliminar() throws Exception {
        Long id = 1L;
        doNothing().when(promocionService).eliminar(id);

        mockMvc.perform(delete("/api/promociones/{id}", id))
                .andExpect(status().isOk());

        verify(promocionService).eliminar(id);
    }
}