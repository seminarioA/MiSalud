package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.dto.PacienteRequest;
import com.medx.beta.dto.PacienteResponse;
import com.medx.beta.service.JwtService;
import com.medx.beta.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PacienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PacienteService pacienteService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void getAll_ok() throws Exception {
        PacienteResponse r = new PacienteResponse();
        r.setPacienteId(1);
        when(pacienteService.getAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pacienteId").value(1));
    }

    @Test
    void getById_ok() throws Exception {
        PacienteResponse r = new PacienteResponse();
        r.setPacienteId(2);
        when(pacienteService.getById(2)).thenReturn(r);

        mockMvc.perform(get("/api/v1/pacientes/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pacienteId").value(2));
    }

    @Test
    void create_created() throws Exception {
        PacienteRequest req = new PacienteRequest();
        req.setPrimerNombre("Ana");
        req.setPrimerApellido("Ruiz");
        req.setSegundoApellido("Lopez");
        req.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        PacienteResponse resp = new PacienteResponse();
        resp.setPacienteId(10);
        when(pacienteService.create(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pacienteId").value(10));
    }

    @Test
    void update_ok() throws Exception {
        PacienteRequest req = new PacienteRequest();
        req.setPrimerNombre("Ana");
        req.setPrimerApellido("Ruiz");
        req.setSegundoApellido("Lopez");
        req.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        PacienteResponse resp = new PacienteResponse();
        resp.setPacienteId(5);
        when(pacienteService.update(eq(5), any())).thenReturn(resp);

        mockMvc.perform(put("/api/v1/pacientes/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pacienteId").value(5));
    }

    @Test
    void delete_noContent() throws Exception {
        doNothing().when(pacienteService).deleteById(3);

        mockMvc.perform(delete("/api/v1/pacientes/{id}", 3))
                .andExpect(status().isNoContent());
    }
}

