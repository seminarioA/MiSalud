package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.exception.GlobalExceptionHandler;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.SedeHospital;
import com.medx.beta.service.DoctorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = DoctorController.class)
@Import({GlobalExceptionHandler.class, DoctorControllerTest.ControllerTestConfig.class})
class DoctorControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Autowired
    DoctorService doctorService;

    @TestConfiguration
    static class ControllerTestConfig {
        @Bean
        @Primary
        DoctorService doctorService() {
            return Mockito.mock(DoctorService.class);
        }
    }

    private Doctor buildDoctor(Integer id) {
        Doctor d = new Doctor();
        d.setDoctorId(id);
        d.setPrimerNombre("John");
        d.setPrimerApellido("Doe");
        d.setSegundoApellido("House");
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(11);
        d.setSedeHospital(sede);
        return d;
    }

    @Test
    @DisplayName("GET /api/doctores devuelve lista 200")
    void list_ok() throws Exception {
        when(doctorService.getAll()).thenReturn(List.of(buildDoctor(1)));
        mvc.perform(get("/api/doctores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId", is(1)))
                .andExpect(jsonPath("$[0].primerNombre", is("John")));
        verify(doctorService).getAll();
    }

    @Test
    @DisplayName("GET /api/doctores/{id} 200")
    void get_ok() throws Exception {
        when(doctorService.getById(5)).thenReturn(buildDoctor(5));
        mvc.perform(get("/api/doctores/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId", is(5)));
        verify(doctorService).getById(5);
    }

    @Test
    @DisplayName("GET /api/doctores/{id} 404 ApiError")
    void get_notFound() throws Exception {
        when(doctorService.getById(99)).thenThrow(new NotFoundException("Doctor no encontrado"));
        mvc.perform(get("/api/doctores/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Doctor no encontrado")));
    }

    @Test
    @DisplayName("POST /api/doctores 201 crea")
    void create_ok() throws Exception {
        Doctor created = buildDoctor(10);
        when(doctorService.create(any(Doctor.class))).thenReturn(created);
        String body = "{" +
                "\"primerNombre\":\"Mario\",\"primerApellido\":\"Rossi\",\"segundoApellido\":\"Bianchi\"," +
                "\"sedeHospital\":{\"sedeId\":11}}";
        mvc.perform(post("/api/doctores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId", is(10)));
        verify(doctorService).create(any(Doctor.class));
    }

    @Test
    @DisplayName("PUT /api/doctores/{id} 200 actualiza")
    void update_ok() throws Exception {
        Doctor updated = buildDoctor(3);
        updated.setPrimerNombre("Nuevo");
        when(doctorService.update(eq(3), any(Doctor.class))).thenReturn(updated);
        String body = mapper.writeValueAsString(updated);
        mvc.perform(put("/api/doctores/3").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.primerNombre", is("Nuevo")));
        verify(doctorService).update(eq(3), any(Doctor.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,5})
    @DisplayName("DELETE /api/doctores/{id} 204")
    void delete_ok(int id) throws Exception {
        mvc.perform(delete("/api/doctores/"+id))
                .andExpect(status().isNoContent());
        verify(doctorService).deleteById(id);
    }
}
