package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.exception.GlobalExceptionHandler;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.CitaMedica;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.Paciente;
import com.medx.beta.service.CitaMedicaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CitaMedicaController.class)
@Import({GlobalExceptionHandler.class, CitaMedicaControllerTest.ControllerTestConfig.class})
class CitaMedicaControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired
    CitaMedicaService citaMedicaService; // reemplaza @MockBean

    private CitaMedica build(Integer id) {
        CitaMedica c = new CitaMedica();
        c.setCitaId(id);
        c.setFecha(LocalDateTime.of(2025,1,1,10,0));
        c.setCosto(new BigDecimal("150.00"));
        Doctor d = new Doctor(); d.setDoctorId(1); c.setDoctor(d);
        Paciente p = new Paciente(); p.setPacienteId(2); c.setPaciente(p);
        return c;
    }

    @Test
    @DisplayName("GET /api/citas 200 lista")
    void list_ok() throws Exception {
        when(citaMedicaService.getAll()).thenReturn(List.of(build(5)));
        mvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].citaId", is(5)))
                .andExpect(jsonPath("$[0].costo", is(150.00)));
        verify(citaMedicaService).getAll();
    }

    @Test
    @DisplayName("GET /api/citas/{id} 200")
    void get_ok() throws Exception {
        when(citaMedicaService.getById(9)).thenReturn(build(9));
        mvc.perform(get("/api/citas/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.citaId", is(9)));
        verify(citaMedicaService).getById(9);
    }

    @Test
    @DisplayName("GET /api/citas/{id} 404")
    void get_notFound() throws Exception {
        when(citaMedicaService.getById(77)).thenThrow(new NotFoundException("Cita no encontrada"));
        mvc.perform(get("/api/citas/77"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Cita")));
    }

    @Test
    @DisplayName("GET /api/citas/doctor/{doctorId} 200")
    void byDoctor_ok() throws Exception {
        when(citaMedicaService.getByDoctor(1)).thenReturn(List.of(build(1)));
        mvc.perform(get("/api/citas/doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctor.doctorId", is(1)));
        verify(citaMedicaService).getByDoctor(1);
    }

    @Test
    @DisplayName("GET /api/citas/paciente/{pacienteId} 200")
    void byPaciente_ok() throws Exception {
        when(citaMedicaService.getByPaciente(2)).thenReturn(List.of(build(3)));
        mvc.perform(get("/api/citas/paciente/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paciente.pacienteId", is(2)));
        verify(citaMedicaService).getByPaciente(2);
    }

    @Test
    @DisplayName("GET /api/citas/rango 200 con parámetros ISO")
    void rango_ok() throws Exception {
        LocalDateTime inicio = LocalDateTime.of(2024,12,31,0,0);
        LocalDateTime fin = LocalDateTime.of(2025,12,31,23,59);
        DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME;
        when(citaMedicaService.getByFechaBetween(inicio, fin)).thenReturn(List.of(build(4)));
        mvc.perform(get("/api/citas/rango")
                        .param("inicio", inicio.format(fmt))
                        .param("fin", fin.format(fmt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].citaId", is(4)));
        verify(citaMedicaService).getByFechaBetween(inicio, fin);
    }

    @Test
    @DisplayName("POST /api/citas 201 crea")
    void create_ok() throws Exception {
        when(citaMedicaService.create(any(CitaMedica.class))).thenReturn(build(20));
        String body = "{\"fecha\":\"2025-01-01T10:00:00\",\"costo\":150.0,\"doctor\":{\"doctorId\":1},\"paciente\":{\"pacienteId\":2}}";
        mvc.perform(post("/api/citas").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.citaId", is(20)));
        verify(citaMedicaService).create(any(CitaMedica.class));
    }

    @Test
    @DisplayName("PUT /api/citas/{id} 200 actualiza")
    void update_ok() throws Exception {
        CitaMedica updated = build(8); updated.setCosto(new BigDecimal("250.00"));
        when(citaMedicaService.update(eq(8), any(CitaMedica.class))).thenReturn(updated);
        String body = mapper.writeValueAsString(updated);
        mvc.perform(put("/api/citas/8").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.costo", is(250.00)));
        verify(citaMedicaService).update(eq(8), any(CitaMedica.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {3,6,9})
    @DisplayName("DELETE /api/citas/{id} 204")
    void delete_ok(int id) throws Exception {
        mvc.perform(delete("/api/citas/"+id))
                .andExpect(status().isNoContent());
        verify(citaMedicaService).deleteById(id);
    }

    @TestConfiguration
    static class ControllerTestConfig {
        @Bean
        @Primary
        CitaMedicaService citaMedicaService() { return Mockito.mock(CitaMedicaService.class); }
    }
}
