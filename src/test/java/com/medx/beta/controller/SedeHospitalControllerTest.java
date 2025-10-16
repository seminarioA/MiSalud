package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.dto.SedeHospitalRequest;
import com.medx.beta.dto.SedeHospitalResponse;
import com.medx.beta.service.JwtService;
import com.medx.beta.service.SedeHospitalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SedeHospitalController.class)
@AutoConfigureMockMvc(addFilters = false)
class SedeHospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SedeHospitalService sedeHospitalService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void getAll_ok() throws Exception {
        SedeHospitalResponse r = new SedeHospitalResponse();
        r.setSedeId(1);
        when(sedeHospitalService.getAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/sedes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sedeId").value(1));
    }

    @Test
    void getById_ok() throws Exception {
        SedeHospitalResponse r = new SedeHospitalResponse();
        r.setSedeId(2);
        when(sedeHospitalService.getById(2)).thenReturn(r);

        mockMvc.perform(get("/api/v1/sedes/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sedeId").value(2));
    }

    @Test
    void getByHospital_ok() throws Exception {
        SedeHospitalResponse r = new SedeHospitalResponse();
        r.setSedeId(3);
        when(sedeHospitalService.getByHospital(7)).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/sedes/hospital/{hospitalId}", 7))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sedeId").value(3));
    }

    @Test
    void create_created() throws Exception {
        SedeHospitalRequest req = new SedeHospitalRequest();
        req.setSede("Central");
        req.setHospitalId(1);
        SedeHospitalResponse resp = new SedeHospitalResponse();
        resp.setSedeId(10);
        when(sedeHospitalService.create(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/sedes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sedeId").value(10));
    }

    @Test
    void update_ok() throws Exception {
        SedeHospitalRequest req = new SedeHospitalRequest();
        req.setSede("Norte");
        req.setHospitalId(1);
        SedeHospitalResponse resp = new SedeHospitalResponse();
        resp.setSedeId(5);
        when(sedeHospitalService.update(eq(5), any())).thenReturn(resp);

        mockMvc.perform(put("/api/v1/sedes/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sedeId").value(5));
    }

    @Test
    void delete_noContent() throws Exception {
        doNothing().when(sedeHospitalService).delete(3);

        mockMvc.perform(delete("/api/v1/sedes/{id}", 3))
                .andExpect(status().isNoContent());
    }
}

