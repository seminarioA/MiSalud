package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.exception.GlobalExceptionHandler;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Especializacion;
import com.medx.beta.service.EspecializacionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = EspecializacionController.class)
@Import({GlobalExceptionHandler.class, EspecializacionControllerTest.ControllerTestConfig.class})
class EspecializacionControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired
    EspecializacionService especializacionService; // reemplaza @MockBean

    private Especializacion build(Integer id) {
        Especializacion e = new Especializacion();
        e.setEspecializacionId(id);
        e.setNombre("Esp " + id);
        e.setDescripcion("Desc " + id);
        return e;
    }

    @Test
    @DisplayName("GET /api/especializaciones 200 lista")
    void list_ok() throws Exception {
        when(especializacionService.getAll()).thenReturn(List.of(build(1)));
        mvc.perform(get("/api/especializaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especializacionId", is(1)));
        verify(especializacionService).getAll();
    }

    @Test
    @DisplayName("GET /api/especializaciones/{id} 200")
    void get_ok() throws Exception {
        when(especializacionService.getById(3)).thenReturn(build(3));
        mvc.perform(get("/api/especializaciones/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especializacionId", is(3)));
        verify(especializacionService).getById(3);
    }

    @Test
    @DisplayName("GET /api/especializaciones/{id} 404")
    void get_notFound() throws Exception {
        when(especializacionService.getById(99)).thenThrow(new NotFoundException("Especializacion no encontrada"));
        mvc.perform(get("/api/especializaciones/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Especializacion")));
    }

    @Test
    @DisplayName("POST /api/especializaciones 201 crea")
    void create_ok() throws Exception {
        when(especializacionService.create(any(Especializacion.class))).thenReturn(build(10));
        String body = "{\"nombre\":\"Cardio\",\"descripcion\":\"Corazon\"}";
        mvc.perform(post("/api/especializaciones").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.especializacionId", is(10)));
        verify(especializacionService).create(any(Especializacion.class));
    }

    @Test
    @DisplayName("PUT /api/especializaciones/{id} 200")
    void update_ok() throws Exception {
        Especializacion updated = build(5); updated.setNombre("Mod");
        when(especializacionService.update(eq(5), any(Especializacion.class))).thenReturn(updated);
        String body = mapper.writeValueAsString(updated);
        mvc.perform(put("/api/especializaciones/5").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Mod")));
        verify(especializacionService).update(eq(5), any(Especializacion.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {2,7})
    @DisplayName("DELETE /api/especializaciones/{id} 204")
    void delete_ok(int id) throws Exception {
        mvc.perform(delete("/api/especializaciones/"+id))
                .andExpect(status().isNoContent());
        verify(especializacionService).deleteById(id);
    }

    @org.springframework.boot.test.context.TestConfiguration
    static class ControllerTestConfig {
        @org.springframework.context.annotation.Bean
        @org.springframework.context.annotation.Primary
        EspecializacionService especializacionService() { return mock(EspecializacionService.class); }
    }
}
