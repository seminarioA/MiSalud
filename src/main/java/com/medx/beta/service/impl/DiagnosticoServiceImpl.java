package com.medx.beta.service.impl;

import com.medx.beta.dto.DiagnosticoRequest;
import com.medx.beta.dto.DiagnosticoResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Diagnostico;
import com.medx.beta.model.RegistroCitaMedica;
import com.medx.beta.repository.DiagnosticoRepository;
import com.medx.beta.repository.RegistroCitaMedicaRepository;
import com.medx.beta.service.DiagnosticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiagnosticoServiceImpl implements DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final RegistroCitaMedicaRepository registroCitaMedicaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticoResponse> findAll() {
        return diagnosticoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticoResponse findById(Long id) {
        Diagnostico diagnostico = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Diagnóstico no encontrado"));
        return toResponse(diagnostico);
    }

    @Override
    public DiagnosticoResponse create(DiagnosticoRequest request) {
        RegistroCitaMedica registro = registroCitaMedicaRepository.findById(request.registroId())
                .orElseThrow(() -> new NotFoundException("Registro médico no encontrado"));
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setRegistro(registro);
        diagnostico.setCodigoIcd10(request.codigoIcd10());
        diagnostico.setDescripcion(request.descripcion());
        return toResponse(diagnosticoRepository.save(diagnostico));
    }

    @Override
    public DiagnosticoResponse update(Long id, DiagnosticoRequest request) {
        Diagnostico diagnostico = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Diagnóstico no encontrado"));
        RegistroCitaMedica registro = registroCitaMedicaRepository.findById(request.registroId())
                .orElseThrow(() -> new NotFoundException("Registro médico no encontrado"));
        diagnostico.setRegistro(registro);
        diagnostico.setCodigoIcd10(request.codigoIcd10());
        diagnostico.setDescripcion(request.descripcion());
        return toResponse(diagnosticoRepository.save(diagnostico));
    }

    @Override
    public void delete(Long id) {
        Diagnostico diagnostico = diagnosticoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Diagnóstico no encontrado"));
        diagnosticoRepository.delete(diagnostico);
    }

    private DiagnosticoResponse toResponse(Diagnostico diagnostico) {
        return new DiagnosticoResponse(
                diagnostico.getId(),
                diagnostico.getRegistro().getId(),
                diagnostico.getCodigoIcd10(),
                diagnostico.getDescripcion()
        );
    }
}

