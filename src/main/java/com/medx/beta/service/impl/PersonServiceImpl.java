package com.medx.beta.service.impl;

import com.medx.beta.dto.PersonaRequest;
import com.medx.beta.dto.PersonaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Persona;
import com.medx.beta.repository.PersonaRepository;
import com.medx.beta.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonaRepository personaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PersonaResponse> findAll() {
        return personaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PersonaResponse findById(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Persona no encontrada"));
        return toResponse(persona);
    }

    @Override
    public PersonaResponse create(PersonaRequest request) {
        if (personaRepository.existsByNumeroDocumento(request.numeroDocumento())) {
            throw new IllegalArgumentException("Ya existe una persona con ese documento");
        }
        Persona persona = new Persona();
        applyRequest(persona, request);
        return toResponse(personaRepository.save(persona));
    }

    @Override
    public PersonaResponse update(Long id, PersonaRequest request) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Persona no encontrada"));
        if (!persona.getNumeroDocumento().equals(request.numeroDocumento()) &&
                personaRepository.existsByNumeroDocumento(request.numeroDocumento())) {
            throw new IllegalArgumentException("Ya existe una persona con ese documento");
        }
        applyRequest(persona, request);
        return toResponse(personaRepository.save(persona));
    }

    @Override
    public void delete(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Persona no encontrada"));
        personaRepository.delete(persona);
    }

    private void applyRequest(Persona persona, PersonaRequest request) {
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

    private PersonaResponse toResponse(Persona persona) {
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
                persona.getUrlFotoPerfil()
        );
    }
}

