package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.dto.EspecializacionRequest;
import com.medx.beta.dto.EspecializacionResponse;
import com.medx.beta.service.EspecializacionService;
import com.medx.beta.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.mock;

@WebMvcTest(controllers = EspecializacionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(EspecializacionControllerTest.MockConfig.class)
class EspecializacionControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        EspecializacionService especializacionService() { return mock(EspecializacionService.class); }
        @Bean
        JwtService jwtService() { return mock(JwtService.class); }
        @Bean
        UserDetailsService userDetailsService() { return mock(UserDetailsService.class); }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EspecializacionService especializacionService;

    @Test
    void getAll_ok() throws Exception {
        EspecializacionResponse r = new EspecializacionResponse(1, "Cardio", "desc");
        when(especializacionService.getAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/especializaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especializacionId").value(1));
    }

    @Test
    void getById_ok() throws Exception {
        EspecializacionResponse r = new EspecializacionResponse(2, "Derma", "d");
        when(especializacionService.getById(2)).thenReturn(r);

        mockMvc.perform(get("/api/v1/especializaciones/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especializacionId").value(2));
    }

    @Test
    void getById_idNegativo_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/especializaciones/{id}", -1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("El id debe ser positivo")));
    }

    @Test
    void create_created() throws Exception {
        EspecializacionRequest req = new EspecializacionRequest();
        req.setNombre("Onco");
        EspecializacionResponse resp = new EspecializacionResponse(10, "Onco", "");
        when(especializacionService.create(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/especializaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.especializacionId").value(10));
    }

    @Test
    void create_requestInvalido_badRequest() throws Exception {
        EspecializacionRequest req = new EspecializacionRequest();

        mockMvc.perform(post("/api/v1/especializaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("El nombre es obligatorio")));
    }

    @Test
    void update_ok() throws Exception {
        EspecializacionRequest req = new EspecializacionRequest();
        req.setNombre("Neuro");
        EspecializacionResponse resp = new EspecializacionResponse(5, "Neuro", "");
        when(especializacionService.update(eq(5), any())).thenReturn(resp);

        mockMvc.perform(put("/api/v1/especializaciones/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especializacionId").value(5));
    }

    @Test
    void delete_noContent() throws Exception {
        doNothing().when(especializacionService).deleteById(3);

        mockMvc.perform(delete("/api/v1/especializaciones/{id}", 3))
                .andExpect(status().isNoContent());
    }
}
