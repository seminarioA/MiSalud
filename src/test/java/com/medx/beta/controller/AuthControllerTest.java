package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.RegistroRequest;
import com.medx.beta.model.Usuario;
import com.medx.beta.service.AuthService;
import com.medx.beta.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private RegistroRequest buildRegistroValido() {
        RegistroRequest r = new RegistroRequest();
        r.setUsername("usuario01");
        r.setEmail("user@example.com");
        r.setPassword("Abcd1234!");
        r.setConfirmarPassword("Abcd1234!");
        r.setNombre("Juan");
        r.setApellido("Pérez");
        return r;
    }

    @Test
    void registrarUsuario_created() throws Exception {
        when(authService.registrarUsuario(any())).thenReturn(new Usuario());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRegistroValido())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Usuario registrado exitosamente"));
    }

    @Test
    void registrarUsuario_badRequest() throws Exception {
        when(authService.registrarUsuario(any())).thenThrow(new IllegalArgumentException("Datos inválidos"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRegistroValido())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Datos inválidos"));
    }

    @Test
    void login_ok() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsernameOrEmail("usuario01");
        req.setPassword("Abcd1234!");
        AuthResponse resp = new AuthResponse("token123", 1L, "usuario01", "user@example.com", "Juan", "Pérez", Usuario.Role.PACIENTE);
        when(authService.autenticar(any())).thenReturn(resp);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"))
                .andExpect(jsonPath("$.username").value("usuario01"));
    }

    @Test
    void login_unauthorized() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsernameOrEmail("usuario01");
        req.setPassword("wrong");
        when(authService.autenticar(any())).thenThrow(new RuntimeException("bad creds"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test
    void me_ok() throws Exception {
        Usuario u = new Usuario();
        u.setUsername("usuario01");
        when(authService.obtenerUsuarioActual()).thenReturn(u);

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario01"));
    }

    @Test
    void me_unauthorized() throws Exception {
        when(authService.obtenerUsuarioActual()).thenThrow(new RuntimeException("no token"));

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token inválido o expirado"));
    }
}
