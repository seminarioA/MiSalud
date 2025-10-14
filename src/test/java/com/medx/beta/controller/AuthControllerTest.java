package com.medx.beta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.RegistroRequest;
import com.medx.beta.model.Usuario;
import com.medx.beta.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("deprecation")

@WebMvcTest(AuthController.class)
@Import({com.medx.beta.config.SecurityDevConfig.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegistroExitoso() throws Exception {
        // Arrange
        RegistroRequest request = new RegistroRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        request.setConfirmarPassword("Password123!");
        request.setNombre("Test");
        request.setApellido("User");

        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1L);
        usuario.setUsername("testuser");

        when(authService.registrarUsuario(any(RegistroRequest.class))).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Usuario registrado exitosamente"));
    }

    @Test
    public void testRegistroConUsuarioExistente() throws Exception {
        // Arrange
        RegistroRequest request = new RegistroRequest();
        request.setUsername("existinguser");
        request.setEmail("existing@example.com");
        request.setPassword("Password123!");
        request.setConfirmarPassword("Password123!");
        request.setNombre("Existing");
        request.setApellido("User");

        when(authService.registrarUsuario(any(RegistroRequest.class)))
                .thenThrow(new IllegalArgumentException("El nombre de usuario ya está en uso"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El nombre de usuario ya está en uso"));
    }

    @Test
    public void testRegistroConDatosInvalidos() throws Exception {
        // Arrange
        RegistroRequest request = new RegistroRequest();
        request.setUsername(""); // Username vacío (inválido)
        request.setEmail("invalid-email"); // Email inválido
        request.setPassword("123"); // Contraseña débil
        request.setNombre("Test");
        request.setApellido("User");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}