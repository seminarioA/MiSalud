package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.exception.GlobalExceptionHandler;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Hospital;
import com.medx.beta.model.SedeHospital;
import com.medx.beta.service.SedeHospitalService;
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

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SedeHospitalController.class)
@Import({GlobalExceptionHandler.class, SedeHospitalControllerTest.ControllerTestConfig.class})
@WithMockUser(username = "tester", roles = {"USER"})
class SedeHospitalControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired SedeHospitalService sedeHospitalService;

    private SedeHospital build(Integer id, Integer hospitalId) {
        Hospital h = new Hospital();
        h.setHospitalId(hospitalId);
        h.setNombre("Hospital " + hospitalId);
        SedeHospital s = new SedeHospital();
        s.setSedeId(id);
        s.setSede("Sede " + id);
        s.setUbicacion("Ubicacion " + id);
        s.setHospital(h);
        return s;
    }

    @Test
    @DisplayName("GET /api/sedes 200 lista")
    void list_ok() throws Exception {
        when(sedeHospitalService.getAll()).thenReturn(List.of(build(1, 10)));
        mvc.perform(get("/api/sedes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sedeId", is(1)))
                .andExpect(jsonPath("$[0].sede", containsString("Sede")));
        verify(sedeHospitalService).getAll();
    }

    @Test
    @DisplayName("GET /api/sedes/{id} 200")
    void get_ok() throws Exception {
        when(sedeHospitalService.getById(5)).thenReturn(build(5, 2));
        mvc.perform(get("/api/sedes/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sedeId", is(5)))
                .andExpect(jsonPath("$.hospital.hospitalId", is(2)));
        verify(sedeHospitalService).getById(5);
    }

    @Test
    @DisplayName("GET /api/sedes/{id} 404")
    void get_notFound() throws Exception {
        when(sedeHospitalService.getById(99)).thenThrow(new NotFoundException("Sede no encontrada"));
        mvc.perform(get("/api/sedes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Sede no encontrada")));
    }

    @Test
    @DisplayName("GET /api/sedes/hospital/{hospitalId} 200 filtra por hospital")
    void byHospital_ok() throws Exception {
        when(sedeHospitalService.getByHospital(7)).thenReturn(List.of(build(3,7)));
        mvc.perform(get("/api/sedes/hospital/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hospital.hospitalId", is(7)));
        verify(sedeHospitalService).getByHospital(7);
    }

    @Test
    @DisplayName("POST /api/sedes 201 crea")
    void create_ok() throws Exception {
        when(sedeHospitalService.create(any(SedeHospital.class))).thenReturn(build(20, 4));
        String body = "{\"sede\":\"Nueva Sede\",\"ubicacion\":\"Zona A\",\"hospital\":{\"hospitalId\":4}}";
        mvc.perform(post("/api/sedes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sedeId", is(20)))
                .andExpect(jsonPath("$.hospital.hospitalId", is(4)));
        verify(sedeHospitalService).create(any(SedeHospital.class));
    }

    @Test
    @DisplayName("PUT /api/sedes/{id} 200 actualiza")
    void update_ok() throws Exception {
        SedeHospital updated = build(8, 3); updated.setSede("Sede Mod");
        when(sedeHospitalService.update(eq(8), any(SedeHospital.class))).thenReturn(updated);
        String body = mapper.writeValueAsString(updated);
        mvc.perform(put("/api/sedes/8").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sede", is("Sede Mod")));
        verify(sedeHospitalService).update(eq(8), any(SedeHospital.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,4,9})
    @DisplayName("DELETE /api/sedes/{id} 204")
    void delete_ok(int id) throws Exception {
        mvc.perform(delete("/api/sedes/"+id).with(csrf()))
                .andExpect(status().isNoContent());
        verify(sedeHospitalService).deleteById(id);
    }

    @TestConfiguration
    static class ControllerTestConfig {
        @Bean
        @Primary
        SedeHospitalService sedeHospitalService() { return Mockito.mock(SedeHospitalService.class); }
    }
}
