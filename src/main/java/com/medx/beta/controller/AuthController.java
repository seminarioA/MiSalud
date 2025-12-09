package com.medx.beta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.AuthUserResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.RegisterPatientRequest;
import com.medx.beta.dto.RegisterPatientResponse;
import com.medx.beta.dto.UsuarioSistemaRequest;
import com.medx.beta.dto.UsuarioSistemaResponse;
import com.medx.beta.service.AuthService;
import com.medx.beta.service.UsuarioSistemaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioSistemaService usuarioSistemaService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.autenticar(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterPatientResponse> register(@Valid @RequestBody RegisterPatientRequest request) {
        return ResponseEntity.ok(authService.registrarPaciente(request));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthUserResponse> me() {
        return ResponseEntity.ok(authService.usuarioActual());
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioSistemaResponse>> usuarios() {
        return ResponseEntity.ok(usuarioSistemaService.findAll());
    }

    @PostMapping("/usuarios")
    // Permitir registro público: cualquiera puede crear su usuario; la lógica en el service evitará
    // la creación de usuarios con rol OPERACIONES salvo que el caller tenga dicho rol.
    public ResponseEntity<UsuarioSistemaResponse> crear(@Valid @RequestBody UsuarioSistemaRequest request) {
        return ResponseEntity.ok(usuarioSistemaService.create(request));
    }

    @PutMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<UsuarioSistemaResponse> actualizar(@PathVariable Long id,
                                                             @Valid @RequestBody UsuarioSistemaRequest request) {
        return ResponseEntity.ok(usuarioSistemaService.update(id, request));
    }

    @DeleteMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioSistemaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
