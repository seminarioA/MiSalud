package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.dto.CitaMedicaRequest;
import com.medx.beta.dto.CitaMedicaResponse;
import com.medx.beta.service.CitaMedicaService;
import com.medx.beta.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CitaMedicaController.class)
@AutoConfigureMockMvc(addFilters = false)
class CitaMedicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CitaMedicaService citaMedicaService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void getAll_ok() throws Exception {
        CitaMedicaResponse r = new CitaMedicaResponse();
        r.setCitaId(1L);
        when(citaMedicaService.getAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].citaId").value(1));
    }

    @Test
    void getById_ok() throws Exception {
        CitaMedicaResponse r = new CitaMedicaResponse();
        r.setCitaId(2L);
        when(citaMedicaService.getById(2)).thenReturn(r);

        mockMvc.perform(get("/api/v1/citas/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.citaId").value(2));
    }

    @Test
    void getByDoctor_ok() throws Exception {
        CitaMedicaResponse r = new CitaMedicaResponse();
        r.setCitaId(3L);
        when(citaMedicaService.getByDoctor(7)).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/citas/doctor/{doctorId}", 7))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].citaId").value(3));
    }

    @Test
    void getByPaciente_ok() throws Exception {
        CitaMedicaResponse r = new CitaMedicaResponse();
        r.setCitaId(4L);
        when(citaMedicaService.getByPaciente(5)).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/citas/paciente/{pacienteId}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].citaId").value(4));
    }

    @Test
    void getByRango_invalidDates_returnsBadRequest() throws Exception {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fin = inicio.minusDays(1);
        mockMvc.perform(get("/api/v1/citas/rango")
                        .param("inicio", inicio.toString())
                        .param("fin", fin.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La fecha de inicio debe ser anterior a la fecha de fin"));
    }

    @Test
    void create_created() throws Exception {
        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setFecha(LocalDateTime.now().plusDays(1));
        req.setTipoCita("CONTROL");
        req.setCosto(new BigDecimal("50.00"));
        req.setDoctorId(1);
        req.setPacienteId(2);
        CitaMedicaResponse resp = new CitaMedicaResponse();
        resp.setCitaId(10L);
        when(citaMedicaService.create(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.citaId").value(10));
    }

    @Test
    void update_ok() throws Exception {
        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setFecha(LocalDateTime.now().plusDays(2));
        req.setTipoCita("URGENCIA");
        req.setCosto(new BigDecimal("100.00"));
        req.setDoctorId(1);
        req.setPacienteId(2);
        CitaMedicaResponse resp = new CitaMedicaResponse();
        resp.setCitaId(11L);
        when(citaMedicaService.update(eq(11), any())).thenReturn(resp);

        mockMvc.perform(put("/api/v1/citas/{id}", 11)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.citaId").value(11));
    }

    @Test
    void delete_noContent() throws Exception {
        doNothing().when(citaMedicaService).deleteById(9);

        mockMvc.perform(delete("/api/v1/citas/{id}", 9))
                .andExpect(status().isNoContent());
    }
}
