package com.medx.beta.service.impl;

import com.medx.beta.dto.PacienteRequest;
import com.medx.beta.dto.PacienteResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> getAll() {
        return pacienteRepository.findAllActivos().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse getById(Integer id) {
        Paciente paciente = pacienteRepository.findByPacienteIdAndEstaActivoTrue(id)
                .orElseThrow(() -> new NotFoundException("Paciente no activo con id: " + id));
        return toResponse(paciente);
    }

    @Override
    public PacienteResponse create(PacienteRequest pacienteRequest) {
        Paciente paciente = new Paciente();
        paciente.setPrimerNombre(pacienteRequest.getPrimerNombre());
        paciente.setSegundoNombre(pacienteRequest.getSegundoNombre());
        paciente.setPrimerApellido(pacienteRequest.getPrimerApellido());
        paciente.setSegundoApellido(pacienteRequest.getSegundoApellido());
        paciente.setFechaNacimiento(pacienteRequest.getFechaNacimiento());
        paciente.setDomicilio(pacienteRequest.getDomicilio());
        Paciente saved = pacienteRepository.save(paciente);
        return toResponse(saved);
    }

    @Override
    public PacienteResponse update(Integer id, PacienteRequest pacienteRequest) {
        Paciente existente = pacienteRepository.findByPacienteIdAndEstaActivoTrue(id)
                .orElseThrow(() -> new NotFoundException("Paciente no activo con id: " + id));
        existente.setPrimerNombre(pacienteRequest.getPrimerNombre());
        existente.setSegundoNombre(pacienteRequest.getSegundoNombre());
        existente.setPrimerApellido(pacienteRequest.getPrimerApellido());
        existente.setSegundoApellido(pacienteRequest.getSegundoApellido());
        existente.setFechaNacimiento(pacienteRequest.getFechaNacimiento());
        existente.setDomicilio(pacienteRequest.getDomicilio());
        Paciente saved = pacienteRepository.save(existente);
        return toResponse(saved);
    }

    @Override
    public void deleteById(Integer id) {
        Paciente existente = pacienteRepository.findByPacienteIdAndEstaActivoTrue(id)
                .orElseThrow(() -> new NotFoundException("Paciente no activo con id: " + id));
        pacienteRepository.desactivarPorId(existente.getPacienteId());
    }

    private PacienteResponse toResponse(Paciente paciente) {
        PacienteResponse dto = new PacienteResponse();
        dto.setPacienteId(paciente.getPacienteId());
        dto.setPrimerNombre(paciente.getPrimerNombre());
        dto.setSegundoNombre(paciente.getSegundoNombre());
        dto.setPrimerApellido(paciente.getPrimerApellido());
        dto.setSegundoApellido(paciente.getSegundoApellido());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setDomicilio(paciente.getDomicilio());
        dto.setEstaActivo(paciente.getEstaActivo());
        dto.setFechaCreacion(paciente.getFechaCreacion());
        dto.setFechaActualizacion(paciente.getFechaActualizacion());
        return dto;
    }
}
