package com.medx.beta.service.impl;

import com.medx.beta.dto.ResponsablePacienteRequest;
import com.medx.beta.dto.ResponsablePacienteResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Paciente;
import com.medx.beta.model.Persona;
import com.medx.beta.model.ResponsablePaciente;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.repository.PersonaRepository;
import com.medx.beta.repository.ResponsablePacienteRepository;
import com.medx.beta.service.ResponsablePacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponsablePacienteServiceImpl implements ResponsablePacienteService {

    private final ResponsablePacienteRepository responsablePacienteRepository;
    private final PacienteRepository pacienteRepository;
    private final PersonaRepository personaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ResponsablePacienteResponse> findAll() {
        return responsablePacienteRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponsablePacienteResponse findById(Long id) {
        ResponsablePaciente responsable = responsablePacienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado"));
        return toResponse(responsable);
    }

    @Override
    public ResponsablePacienteResponse create(ResponsablePacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
        Persona persona = personaRepository.findById(request.personaResponsableId())
                .orElseThrow(() -> new NotFoundException("Persona responsable no encontrada"));
        ResponsablePaciente responsable = new ResponsablePaciente();
        responsable.setPaciente(paciente);
        responsable.setPersonaResponsable(persona);
        responsable.setRelacion(request.relacion());
        return toResponse(responsablePacienteRepository.save(responsable));
    }

    @Override
    public ResponsablePacienteResponse update(Long id, ResponsablePacienteRequest request) {
        ResponsablePaciente responsable = responsablePacienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado"));
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
        Persona persona = personaRepository.findById(request.personaResponsableId())
                .orElseThrow(() -> new NotFoundException("Persona responsable no encontrada"));
        responsable.setPaciente(paciente);
        responsable.setPersonaResponsable(persona);
        responsable.setRelacion(request.relacion());
        return toResponse(responsablePacienteRepository.save(responsable));
    }

    @Override
    public void delete(Long id) {
        ResponsablePaciente responsable = responsablePacienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Responsable no encontrado"));
        responsablePacienteRepository.delete(responsable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponsablePacienteResponse> findByPaciente(Long pacienteId) {
        return responsablePacienteRepository.findAll().stream()
                .filter(r -> r.getPaciente().getId().equals(pacienteId))
                .map(this::toResponse)
                .toList();
    }

    private ResponsablePacienteResponse toResponse(ResponsablePaciente responsable) {
        return new ResponsablePacienteResponse(
                responsable.getId(),
                responsable.getPaciente().getId(),
                responsable.getPersonaResponsable().getId(),
                responsable.getRelacion()
        );
    }
}

