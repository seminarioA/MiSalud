package com.medx.beta.service.impl;

import com.medx.beta.dto.PersonaRequest;
import com.medx.beta.dto.PersonaResponse;
import com.medx.beta.dto.UsuarioSistemaRequest;
import com.medx.beta.dto.UsuarioSistemaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Persona;
import com.medx.beta.model.UsuarioSistema;
import com.medx.beta.repository.PersonaRepository;
import com.medx.beta.repository.UsuarioSistemaRepository;
import com.medx.beta.service.PersonService;
import com.medx.beta.service.UsuarioSistemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioSistemaServiceImpl implements UsuarioSistemaService {

    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final PersonaRepository personaRepository;
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioSistemaResponse> findAll() {
        return usuarioSistemaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioSistemaResponse findById(Long id) {
        UsuarioSistema usuario = usuarioSistemaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario del sistema no encontrado"));
        return toResponse(usuario);
    }

    @Override
    public UsuarioSistemaResponse create(UsuarioSistemaRequest request) {
        Persona persona = new Persona();
        applyPersona(persona, request.persona());
        personaRepository.save(persona);
        if (usuarioSistemaRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        UsuarioSistema usuario = new UsuarioSistema();
        usuario.setPersona(persona);
        usuario.setEmail(request.email());
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));

        // Determinar rol: si el caller tiene ROLE_OPERACIONES, permitir el rol del request;
        // de lo contrario forzar rol PACIENTE
        UsuarioSistema.Rol requestedRol = request.rol();
        if (callerHasRoleOperaciones()) {
            usuario.setRol(requestedRol);
        } else {
            usuario.setRol(UsuarioSistema.Rol.PACIENTE);
        }

        return toResponse(usuarioSistemaRepository.save(usuario));
    }

    @Override
    public UsuarioSistemaResponse update(Long id, UsuarioSistemaRequest request) {
        UsuarioSistema usuario = usuarioSistemaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario del sistema no encontrado"));
        Persona persona = usuario.getPersona();
        applyPersona(persona, request.persona());
        personaRepository.save(persona);
        if (!usuario.getEmail().equals(request.email()) &&
                usuarioSistemaRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        usuario.setEmail(request.email());
        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        // Solo ROLE_OPERACIONES puede cambiar el rol
        if (callerHasRoleOperaciones()) {
            usuario.setRol(request.rol());
        }

        return toResponse(usuarioSistemaRepository.save(usuario));
    }

    @Override
    public void delete(Long id) {
        UsuarioSistema usuario = usuarioSistemaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario del sistema no encontrado"));
        usuarioSistemaRepository.delete(usuario);
    }

    private UsuarioSistemaResponse toResponse(UsuarioSistema usuario) {
        PersonaResponse persona = personService.findById(usuario.getPersona().getId());
        return new UsuarioSistemaResponse(
                usuario.getId(),
                persona,
                usuario.getEmail(),
                usuario.getRol()
        );
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

    private boolean callerHasRoleOperaciones() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return false;
        for (GrantedAuthority ga : auth.getAuthorities()) {
            if (ga.getAuthority().equals("ROLE_OPERACIONES")) return true;
        }
        return false;
    }
}
