package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.exception.GlobalExceptionHandler;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Paciente;
import com.medx.beta.service.PacienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PacienteController.class)
@Import({GlobalExceptionHandler.class, PacienteControllerTest.ControllerTestConfig.class})
@WithMockUser(username = "tester", roles = {"USER"})
class PacienteControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired
    PacienteService pacienteService; // inyectado desde ControllerTestConfig

    @TestConfiguration
    static class ControllerTestConfig {
        @Bean
        @Primary
        PacienteService pacienteService() {
            return Mockito.mock(PacienteService.class);
        }
    }

    private Paciente buildPaciente(Integer id) {
        Paciente p = new Paciente();
        p.setPacienteId(id);
        p.setPrimerNombre("Ana");
        p.setPrimerApellido("Lopez");
        p.setSegundoApellido("Diaz");
        p.setFechaNacimiento(LocalDate.of(1990,1,1));
        p.setEstaActivo(true);
        return p;
    }

    @Test
    @DisplayName("GET /api/pacientes lista 200")
    void list_ok() throws Exception {
        when(pacienteService.getAll()).thenReturn(List.of(buildPaciente(1)));
        mvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pacienteId", is(1)));
        verify(pacienteService).getAll();
    }

    @Test
    @DisplayName("GET /api/pacientes/{id} 200")
    void get_ok() throws Exception {
        when(pacienteService.getById(3)).thenReturn(buildPaciente(3));
        mvc.perform(get("/api/pacientes/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pacienteId", is(3)));
        verify(pacienteService).getById(3);
    }

    @Test
    @DisplayName("GET /api/pacientes/{id} 404")
    void get_notFound() throws Exception {
        when(pacienteService.getById(99)).thenThrow(new NotFoundException("Paciente no encontrado"));
        mvc.perform(get("/api/pacientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Paciente no encontrado")));
    }

    @Test
    @DisplayName("POST /api/pacientes 201")
    void create_ok() throws Exception {
        Paciente creado = buildPaciente(10);
        when(pacienteService.create(any(Paciente.class))).thenReturn(creado);
        String body = "{\"primerNombre\":\"Ana\",\"primerApellido\":\"Lopez\",\"segundoApellido\":\"Diaz\",\"estaActivo\":true}";
        mvc.perform(post("/api/pacientes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pacienteId", is(10)));
        verify(pacienteService).create(any(Paciente.class));
    }

    @Test
    @DisplayName("PUT /api/pacientes/{id} 200")
    void update_ok() throws Exception {
        Paciente updated = buildPaciente(4);
        updated.setPrimerNombre("AnaMaria");
        when(pacienteService.update(eq(4), any(Paciente.class))).thenReturn(updated);
        String body = mapper.writeValueAsString(updated);
        mvc.perform(put("/api/pacientes/4").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.primerNombre", is("AnaMaria")));
        verify(pacienteService).update(eq(4), any(Paciente.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,7})
    @DisplayName("DELETE /api/pacientes/{id} 204")
    void delete_ok(int id) throws Exception {
        mvc.perform(delete("/api/pacientes/"+id).with(csrf()))
                .andExpect(status().isNoContent());
        verify(pacienteService).deleteById(id);
    }
}
