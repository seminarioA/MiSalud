package com.medx.beta.service.impl;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.AuthUserResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.PacienteResponse;
import com.medx.beta.dto.PersonaRequest;
import com.medx.beta.dto.PersonaResponse;
import com.medx.beta.dto.RegisterPatientRequest;
import com.medx.beta.dto.RegisterPatientResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.UsuarioSistema;
import com.medx.beta.model.Paciente;
import com.medx.beta.model.Persona;
import com.medx.beta.model.Seguro;
import com.medx.beta.repository.UsuarioSistemaRepository;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.repository.PersonaRepository;
import com.medx.beta.repository.SeguroRepository;
import com.medx.beta.service.AuthService;
import com.medx.beta.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

        private final UsuarioSistemaRepository usuarioSistemaRepository;
        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final PersonaRepository personaRepository;
        private final PacienteRepository pacienteRepository;
        private final SeguroRepository seguroRepository;
        private final PasswordEncoder passwordEncoder;

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

        @Override
        public RegisterPatientResponse registrarPaciente(RegisterPatientRequest request) {
                if (usuarioSistemaRepository.findByEmail(request.email()).isPresent()) {
                        throw new IllegalArgumentException("El email ya está registrado");
                }
                PersonaRequest personaRequest = request.persona();
                if (personaRepository.existsByNumeroDocumento(personaRequest.numeroDocumento())) {
                        throw new IllegalArgumentException("Ya existe una persona con ese documento");
                }

                Persona persona = new Persona();
                applyPersona(persona, personaRequest);
                Persona savedPersona = personaRepository.save(persona);

                Paciente paciente = Paciente.builder()
                                .persona(savedPersona)
                                .build();
                if (request.seguroId() != null) {
                        Seguro seguro = seguroRepository.findById(request.seguroId())
                                        .orElseThrow(() -> new NotFoundException("Seguro no encontrado"));
                        paciente.setSeguro(seguro);
                }
                Paciente savedPaciente = pacienteRepository.save(paciente);

                UsuarioSistema usuario = UsuarioSistema.builder()
                                .persona(savedPersona)
                                .email(request.email())
                                .passwordHash(passwordEncoder.encode(request.password()))
                                .rol(UsuarioSistema.Rol.PACIENTE)
                                .build();
                UsuarioSistema savedUsuario = usuarioSistemaRepository.save(usuario);

                String token = jwtService.generateToken(savedUsuario,
                                Map.of("rol", savedUsuario.getRol().name()));
                AuthUserResponse userResponse = new AuthUserResponse(
                                savedUsuario.getId(),
                                savedUsuario.getEmail(),
                                savedUsuario.getRol());
                AuthResponse authResponse = new AuthResponse(token, userResponse);

                PacienteResponse pacienteResponse = toPacienteResponse(savedPaciente);
                return new RegisterPatientResponse(authResponse, pacienteResponse);
        }

        private void applyPersona(Persona persona, PersonaRequest request) {
                persona.setPrimerNombre(request.primerNombre());
                persona.setSegundoNombre(request.segundoNombre());
                persona.setPrimerApellido(request.primerApellido());
                persona.setSegundoApellido(request.segundoApellido());
                persona.setTipoDocumento(request.tipoDocumento());
                persona.setNumeroDocumento(request.numeroDocumento());
                persona.setFechaNacimiento(request.fechaNacimiento());
                persona.setGenero(request.genero());
                persona.setNumeroTelefono(request.numeroTelefono());
                persona.setUrlFotoPerfil(request.urlFotoPerfil());
        }

        private PersonaResponse toPersonaResponse(Persona persona) {
                return new PersonaResponse(
                                persona.getId(),
                                persona.getPrimerNombre(),
                                persona.getSegundoNombre(),
                                persona.getPrimerApellido(),
                                persona.getSegundoApellido(),
                                persona.getTipoDocumento(),
                                persona.getNumeroDocumento(),
                                persona.getFechaNacimiento(),
                                persona.getGenero(),
                                persona.getNumeroTelefono(),
                                persona.getUrlFotoPerfil());
        }

        private PacienteResponse toPacienteResponse(Paciente paciente) {
                PersonaResponse persona = toPersonaResponse(paciente.getPersona());
                Seguro seguro = paciente.getSeguro();
                return new PacienteResponse(
                                paciente.getId(),
                                persona,
                                seguro != null ? seguro.getId() : null,
                                seguro != null ? seguro.getNombreAseguradora() : null);
        }
}
