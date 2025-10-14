package com.medx.beta.controller;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.MessageResponse;
import com.medx.beta.dto.RegistroRequest;
import com.medx.beta.model.Usuario;
import com.medx.beta.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegistroRequest registroRequest) {
        try {
            Usuario usuario = authService.registrarUsuario(registroRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse("Usuario registrado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error interno del servidor"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.autenticar(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Credenciales inválidas"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> obtenerPerfilActual() {
        try {
            Usuario usuario = authService.obtenerUsuarioActual();
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Token inválido o expirado"));
        }
    }
}