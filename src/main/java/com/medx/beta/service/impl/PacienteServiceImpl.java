package com.medx.beta.service.impl;

import com.medx.beta.dto.PacienteRequest;
import com.medx.beta.dto.PacienteResponse;
import com.medx.beta.dto.PersonaRequest;
import com.medx.beta.dto.PersonaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Paciente;
import com.medx.beta.model.Persona;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.repository.PersonaRepository;
import com.medx.beta.service.PacienteService;
import com.medx.beta.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PersonaRepository personaRepository;
    private final PersonService personService;

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> findAll() {
        return pacienteRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse findById(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
        return toResponse(paciente);
    }

    private final com.medx.beta.repository.SeguroRepository seguroRepository;

    @Override
    public PacienteResponse create(PacienteRequest request) {
        Persona persona = new Persona();
        applyPersona(persona, request.persona());
        Persona savedPersona = personaRepository.save(persona);

        Paciente paciente = new Paciente();
        paciente.setPersona(savedPersona);

        com.medx.beta.model.Seguro seguro = seguroRepository.findById(request.seguroId())
                .orElseThrow(() -> new NotFoundException("Seguro no encontrado"));
        paciente.setSeguro(seguro);

        return toResponse(pacienteRepository.save(paciente));
    }

    @Override
    public PacienteResponse update(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
        Persona persona = paciente.getPersona();
        applyPersona(persona, request.persona());
        personaRepository.save(persona);

        com.medx.beta.model.Seguro seguro = seguroRepository.findById(request.seguroId())
                .orElseThrow(() -> new NotFoundException("Seguro no encontrado"));
        paciente.setSeguro(seguro);

        return toResponse(pacienteRepository.save(paciente));
    }

    @Override
    public void delete(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
        pacienteRepository.delete(paciente);
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

    private PacienteResponse toResponse(Paciente paciente) {
        PersonaResponse persona = personService.findById(paciente.getPersona().getId());
        return new PacienteResponse(
                paciente.getId(),
                persona,
                paciente.getSeguro() != null ? paciente.getSeguro().getId() : null,
                paciente.getSeguro() != null ? paciente.getSeguro().getNombreAseguradora() : null);
    }
}
