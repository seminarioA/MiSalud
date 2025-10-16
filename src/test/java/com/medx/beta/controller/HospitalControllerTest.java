package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.dto.HospitalRequest;
import com.medx.beta.dto.HospitalResponse;
import com.medx.beta.service.HospitalService;
import com.medx.beta.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(controllers = HospitalController.class)
@AutoConfigureMockMvc(addFilters = false)
class HospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HospitalService hospitalService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void getAll_ok() throws Exception {
        HospitalResponse r = new HospitalResponse();
        r.setHospitalId(1);
        when(hospitalService.getAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/hospitales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hospitalId").value(1));
    }

    @Test
    void getById_ok() throws Exception {
        HospitalResponse r = new HospitalResponse();
        r.setHospitalId(2);
        when(hospitalService.getById(2)).thenReturn(r);

        mockMvc.perform(get("/api/v1/hospitales/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalId").value(2));
    }

    @Test
    void getById_idNegativo_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/hospitales/{id}", -5))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("El id debe ser positivo")));
    }

    @Test
    void create_created() throws Exception {
        HospitalRequest req = new HospitalRequest();
        req.setNombre("Hosp A");
        HospitalResponse resp = new HospitalResponse();
        resp.setHospitalId(10);
        when(hospitalService.create(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/hospitales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hospitalId").value(10));
    }

    @Test
    void create_requestInvalido_badRequest() throws Exception {
        HospitalRequest req = new HospitalRequest();

        mockMvc.perform(post("/api/v1/hospitales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("nombre")))
                .andExpect(jsonPath("$.message").value(containsString("must not be blank")));
    }

    @Test
    void update_ok() throws Exception {
        HospitalRequest req = new HospitalRequest();
        req.setNombre("Hosp B");
        HospitalResponse resp = new HospitalResponse();
        resp.setHospitalId(5);
        when(hospitalService.update(eq(5), any())).thenReturn(resp);

        mockMvc.perform(put("/api/v1/hospitales/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalId").value(5));
    }

    @Test
    void delete_noContent() throws Exception {
        doNothing().when(hospitalService).deleteById(3);

        mockMvc.perform(delete("/api/v1/hospitales/{id}", 3))
                .andExpect(status().isNoContent());
    }
}
