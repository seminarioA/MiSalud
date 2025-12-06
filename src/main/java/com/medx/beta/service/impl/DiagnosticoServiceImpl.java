package com.medx.beta.service.impl;

import com.medx.beta.dto.DiagnosticoRequest;
import com.medx.beta.dto.DiagnosticoResponse;
import com.medx.beta.model.Cita;
import com.medx.beta.model.Diagnostico;
import com.medx.beta.model.HistoriaClinica;
import com.medx.beta.repository.CitaRepository;
import com.medx.beta.repository.DiagnosticoRepository;
import com.medx.beta.repository.HistoriaClinicaRepository;
import com.medx.beta.service.DiagnosticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiagnosticoServiceImpl implements DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final CitaRepository citaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;

    @Override
    public DiagnosticoResponse create(DiagnosticoRequest request) {
        Cita cita = citaRepository.findById(request.citaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        // Find or create Historia Clinical for Patient if not explicitly linked (Logic
        // assumption: 1-1 Patient-Historia)
        // For now, valid logic: Find historia by patient ID from Cita
        HistoriaClinica historia = historiaClinicaRepository.findByPacienteId(cita.getPaciente().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El paciente no tiene historia clinica activa"));

        Diagnostico diagnostico = Diagnostico.builder()
                .cita(cita)
                .historiaClinica(historia)
                .cie10(request.cie10())
                .descripcion(request.descripcion())
                .tipo(request.tipo())
                .fechaDiagnostico(
                        request.fechaDiagnostico() != null ? request.fechaDiagnostico() : java.time.LocalDateTime.now())
                .build();

        diagnostico = diagnosticoRepository.save(diagnostico);
        return mapToResponse(diagnostico);
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticoResponse findById(Long id) {
        return diagnosticoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticoResponse> findByCitaId(Long citaId) {
        return diagnosticoRepository.findByCitaId(citaId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticoResponse> findByHistoriaClinicaId(Long historiaId) {
        return diagnosticoRepository.findByHistoriaClinicaId(historiaId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!diagnosticoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico no encontrado");
        }
        diagnosticoRepository.deleteById(id);
    }

    private DiagnosticoResponse mapToResponse(Diagnostico diagnostico) {
        return new DiagnosticoResponse(
                diagnostico.getId(),
                diagnostico.getCita().getId(),
                diagnostico.getHistoriaClinica().getId(),
                diagnostico.getCie10(),
                diagnostico.getDescripcion(),
                diagnostico.getTipo(),
                diagnostico.getFechaDiagnostico());
    }
}
