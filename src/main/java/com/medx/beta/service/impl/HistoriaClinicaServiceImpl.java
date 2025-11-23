package com.medx.beta.service.impl;

import com.medx.beta.dto.HistoriaClinicaRequest;
import com.medx.beta.dto.HistoriaClinicaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.HistoriaClinica;
import com.medx.beta.model.Paciente;
import com.medx.beta.repository.HistoriaClinicaRepository;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.service.HistoriaClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoriaClinicaServiceImpl implements HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HistoriaClinicaResponse> findAll() {
        return historiaClinicaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HistoriaClinicaResponse findById(Long id) {
        HistoriaClinica historia = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Historia clínica no encontrada"));
        return toResponse(historia);
    }

    @Override
    public HistoriaClinicaResponse create(HistoriaClinicaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
        HistoriaClinica historia = new HistoriaClinica();
        historia.setPaciente(paciente);
        historia.setFechaApertura(LocalDateTime.now());
        return toResponse(historiaClinicaRepository.save(historia));
    }

    @Override
    public HistoriaClinicaResponse update(Long id, HistoriaClinicaRequest request) {
        HistoriaClinica historia = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Historia clínica no encontrada"));
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
        historia.setPaciente(paciente);
        return toResponse(historiaClinicaRepository.save(historia));
    }

    @Override
    public void delete(Long id) {
        HistoriaClinica historia = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Historia clínica no encontrada"));
        historiaClinicaRepository.delete(historia);
    }

    @Override
    @Transactional(readOnly = true)
    public HistoriaClinicaResponse findByPaciente(Long pacienteId) {
        return historiaClinicaRepository.findAll().stream()
                .filter(h -> h.getPaciente().getId().equals(pacienteId))
                .map(this::toResponse)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Historia clínica no encontrada para el paciente"));
    }

    private HistoriaClinicaResponse toResponse(HistoriaClinica historia) {
        return new HistoriaClinicaResponse(
                historia.getId(),
                historia.getPaciente().getId(),
                historia.getFechaApertura()
        );
    }
}

