package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.dto.DoctorRequest;
import com.medx.beta.dto.DoctorResponse;
import com.medx.beta.service.DoctorService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoctorController.class, properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc(addFilters = false)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    // Mock de dependencias de seguridad requeridas por JwtAuthenticationFilter
    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void listar_ok() throws Exception {
        DoctorResponse r = new DoctorResponse();
        r.setDoctorId(1);
        when(doctorService.getAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/doctores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(1));
    }

    @Test
    void obtener_ok() throws Exception {
        DoctorResponse r = new DoctorResponse();
        r.setDoctorId(7);
        when(doctorService.getById(7)).thenReturn(r);

        mockMvc.perform(get("/api/v1/doctores/{id}", 7))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(7));
    }

    @Test
    void crear_created() throws Exception {
        DoctorRequest req = new DoctorRequest();
        req.setPrimerNombre("Ana");
        req.setPrimerApellido("Ruiz");
        req.setSegundoApellido("Lopez");
        req.setSedeId(1);
        req.setTurnoId(2);
        DoctorResponse resp = new DoctorResponse();
        resp.setDoctorId(10);
        when(doctorService.create(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/doctores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId").value(10));
    }

    @Test
    void actualizar_ok() throws Exception {
        DoctorRequest req = new DoctorRequest();
        req.setPrimerNombre("Ana");
        req.setPrimerApellido("Ruiz");
        req.setSegundoApellido("Lopez");
        req.setSedeId(1);
        req.setTurnoId(2);
        DoctorResponse resp = new DoctorResponse();
        resp.setDoctorId(5);
        when(doctorService.update(eq(5), any())).thenReturn(resp);

        mockMvc.perform(put("/api/v1/doctores/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(5));
    }

    @Test
    void eliminar_noContent() throws Exception {
        doNothing().when(doctorService).deleteById(3);

        mockMvc.perform(delete("/api/v1/doctores/{id}", 3))
                .andExpect(status().isNoContent());

        verify(doctorService).deleteById(3);
    }
}
