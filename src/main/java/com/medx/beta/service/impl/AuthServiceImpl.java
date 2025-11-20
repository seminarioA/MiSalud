package com.medx.beta.service.impl;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.AuthUserResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.RegistroRequest;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Usuario;
import com.medx.beta.repository.UsuarioRepository;
import com.medx.beta.service.AuthService;
import com.medx.beta.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public Usuario registrarUsuario(RegistroRequest registroRequest) {
        // Validar que las contraseñas coincidan
        if (!registroRequest.getPassword().equals(registroRequest.getConfirmarPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        // Verificar si el usuario ya existe
        if (usuarioRepository.existsByUsername(registroRequest.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(registroRequest.getUsername());
        usuario.setEmail(registroRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
        usuario.setNombre(registroRequest.getNombre());
        usuario.setApellido(registroRequest.getApellido());

        if (registroRequest.getRol() != null && !registroRequest.getRol().trim().isEmpty()) {
            usuario.setRol(Usuario.Role.valueOf(registroRequest.getRol().toUpperCase()));
        } else {
            usuario.setRol(Usuario.Role.PACIENTE);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public AuthResponse autenticar(LoginRequest loginRequest) {
        // Buscar usuario por username o email
        Usuario usuario = usuarioRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(), 
                loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (!Boolean.TRUE.equals(usuario.getEstaActivo())) {
            throw new IllegalStateException("La cuenta está inactiva");
        }

        // Autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuario.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Generar token
        String token = jwtService.generateToken(usuario);

        return new AuthResponse(
                token,
                usuario.getUsuarioId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRol()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserResponse obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Token inválido o expirado");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Usuario usuarioAutenticado) {
            return toAuthUserResponse(usuarioAutenticado);
        }

        if (principal instanceof String principalName && "anonymousUser".equalsIgnoreCase(principalName)) {
            throw new IllegalStateException("Token inválido o expirado");
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return toAuthUserResponse(usuario);
    }

    private AuthUserResponse toAuthUserResponse(Usuario usuario) {
        return new AuthUserResponse(
                usuario.getUsuarioId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRol(),
                usuario.getEstaActivo()
        );
    }
}