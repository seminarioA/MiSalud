package com.medx.beta.service.impl;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.AuthUserResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.UsuarioSistema;
import com.medx.beta.repository.UsuarioSistemaRepository;
import com.medx.beta.service.AuthService;
import com.medx.beta.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final UsuarioSistemaRepository usuarioSistemaRepository;
        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;

        @Override
        public AuthResponse autenticar(LoginRequest request) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
                UsuarioSistema usuario = usuarioSistemaRepository.findByEmail(request.email())
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
                String token = jwtService.generateToken((UserDetails) authentication.getPrincipal(),
                                Map.of("rol", usuario.getRol().name()));
                AuthUserResponse userResponse = new AuthUserResponse(
                                usuario.getId(),
                                usuario.getEmail(),
                                usuario.getRol());
                return new AuthResponse(token, userResponse);
        }

        @Override
        public AuthUserResponse usuarioActual() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                        throw new IllegalStateException("No hay usuario autenticado");
                }
                String email = authentication.getName();
                UsuarioSistema usuario = usuarioSistemaRepository.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
                return new AuthUserResponse(usuario.getId(), usuario.getEmail(), usuario.getRol());
        }
}
