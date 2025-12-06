package com.medx.beta.service.impl;

import com.medx.beta.dto.TratamientoRequest;
import com.medx.beta.dto.TratamientoResponse;
import com.medx.beta.model.Diagnostico;
import com.medx.beta.model.Tratamiento;
import com.medx.beta.repository.DiagnosticoRepository;
import com.medx.beta.repository.TratamientoRepository;
import com.medx.beta.service.TratamientoService;
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
public class TratamientoServiceImpl implements TratamientoService {

    private final TratamientoRepository tratamientoRepository;
    private final DiagnosticoRepository diagnosticoRepository;

    @Override
    public TratamientoResponse create(TratamientoRequest request) {
        Diagnostico diagnostico = diagnosticoRepository.findById(request.diagnosticoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico no encontrado"));

        Tratamiento tratamiento = Tratamiento.builder()
                .diagnostico(diagnostico)
                .descripcion(request.descripcion())
                .fechaInicio(request.fechaInicio())
                .fechaFin(request.fechaFin())
                .estado(request.estado())
                .build();

        tratamiento = tratamientoRepository.save(tratamiento);
        return mapToResponse(tratamiento);
    }

    @Override
    @Transactional(readOnly = true)
    public TratamientoResponse findById(Long id) {
        return tratamientoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tratamiento no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TratamientoResponse> findByDiagnosticoId(Long diagnosticoId) {
        if (!diagnosticoRepository.existsById(diagnosticoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico no encontrado");
        }
        return tratamientoRepository.findByDiagnosticoId(diagnosticoId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!tratamientoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tratamiento no encontrado");
        }
        tratamientoRepository.deleteById(id);
    }

    private TratamientoResponse mapToResponse(Tratamiento tratamiento) {
        return new TratamientoResponse(
                tratamiento.getId(),
                tratamiento.getDiagnostico().getId(),
                tratamiento.getDescripcion(),
                tratamiento.getFechaInicio(),
                tratamiento.getFechaFin(),
                tratamiento.getEstado());
    }
}
