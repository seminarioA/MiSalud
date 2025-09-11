package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.exception.GlobalExceptionHandler;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Hospital;
import com.medx.beta.service.HospitalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HospitalController.class)
@Import({GlobalExceptionHandler.class, HospitalControllerTest.ControllerTestConfig.class})
class HospitalControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired
    HospitalService hospitalService;

    private Hospital build(Integer id) {
        Hospital h = new Hospital();
        h.setHospitalId(id);
        h.setNombre("General " + id);
        h.setDescripcion("Desc" + id);
        return h;
    }

    @Test
    @DisplayName("GET /api/hospitales 200 lista")
    void list_ok() throws Exception {
        when(hospitalService.getAll()).thenReturn(List.of(build(1)));
        mvc.perform(get("/api/hospitales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hospitalId", is(1)))
                .andExpect(jsonPath("$[0].nombre", containsString("General")));
        verify(hospitalService).getAll();
    }

    @Test
    @DisplayName("GET /api/hospitales/{id} 200")
    void get_ok() throws Exception {
        when(hospitalService.getById(3)).thenReturn(build(3));
        mvc.perform(get("/api/hospitales/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalId", is(3)));
        verify(hospitalService).getById(3);
    }

    @Test
    @DisplayName("GET /api/hospitales/{id} 404")
    void get_notFound() throws Exception {
        when(hospitalService.getById(99)).thenThrow(new NotFoundException("Hospital no encontrado"));
        mvc.perform(get("/api/hospitales/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Hospital no encontrado")));
    }

    @Test
    @DisplayName("POST /api/hospitales 201")
    void create_ok() throws Exception {
        when(hospitalService.create(any(Hospital.class))).thenReturn(build(10));
        String body = "{\"nombre\":\"Nuevo\",\"descripcion\":\"Desc\"}";
        mvc.perform(post("/api/hospitales").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hospitalId", is(10)));
        verify(hospitalService).create(any(Hospital.class));
    }

    @Test
    @DisplayName("PUT /api/hospitales/{id} 200")
    void update_ok() throws Exception {
        Hospital updated = build(5); updated.setNombre("Actualizado");
        when(hospitalService.update(eq(5), any(Hospital.class))).thenReturn(updated);
        String body = mapper.writeValueAsString(updated);
        mvc.perform(put("/api/hospitales/5").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Actualizado")));
        verify(hospitalService).update(eq(5), any(Hospital.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {2,4})
    @DisplayName("DELETE /api/hospitales/{id} 204")
    void delete_ok(int id) throws Exception {
        mvc.perform(delete("/api/hospitales/"+id))
                .andExpect(status().isNoContent());
        verify(hospitalService).deleteHospital(id);
    }

    @org.springframework.boot.test.context.TestConfiguration
    static class ControllerTestConfig {
        @org.springframework.context.annotation.Bean
        @org.springframework.context.annotation.Primary
        HospitalService hospitalService() { return Mockito.mock(HospitalService.class); }
    }
}
