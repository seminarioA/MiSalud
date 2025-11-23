package com.medx.beta.service.impl;

import com.medx.beta.dto.RegistroCitaMedicaRequest;
import com.medx.beta.dto.RegistroCitaMedicaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Cita;
import com.medx.beta.model.HistoriaClinica;
import com.medx.beta.model.RegistroCitaMedica;
import com.medx.beta.repository.CitaRepository;
import com.medx.beta.repository.HistoriaClinicaRepository;
import com.medx.beta.repository.RegistroCitaMedicaRepository;
import com.medx.beta.service.RegistroCitaMedicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistroCitaMedicaServiceImpl implements RegistroCitaMedicaService {

    private final RegistroCitaMedicaRepository registroCitaMedicaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final CitaRepository citaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RegistroCitaMedicaResponse> findAll() {
        return registroCitaMedicaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RegistroCitaMedicaResponse findById(Long id) {
        RegistroCitaMedica registro = registroCitaMedicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro de cita no encontrado"));
        return toResponse(registro);
    }

    @Override
    public RegistroCitaMedicaResponse create(RegistroCitaMedicaRequest request) {
        HistoriaClinica historia = historiaClinicaRepository.findById(request.historiaClinicaId())
                .orElseThrow(() -> new NotFoundException("Historia clínica no encontrada"));
        Cita cita = citaRepository.findById(request.citaId())
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        RegistroCitaMedica registro = new RegistroCitaMedica();
        registro.setHistoriaClinica(historia);
        registro.setCita(cita);
        registro.setNotasMedicas(request.notasMedicas());
        return toResponse(registroCitaMedicaRepository.save(registro));
    }

    @Override
    public RegistroCitaMedicaResponse update(Long id, RegistroCitaMedicaRequest request) {
        RegistroCitaMedica registro = registroCitaMedicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro de cita no encontrado"));
        HistoriaClinica historia = historiaClinicaRepository.findById(request.historiaClinicaId())
                .orElseThrow(() -> new NotFoundException("Historia clínica no encontrada"));
        Cita cita = citaRepository.findById(request.citaId())
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        registro.setHistoriaClinica(historia);
        registro.setCita(cita);
        registro.setNotasMedicas(request.notasMedicas());
        return toResponse(registroCitaMedicaRepository.save(registro));
    }

    @Override
    public void delete(Long id) {
        RegistroCitaMedica registro = registroCitaMedicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Registro de cita no encontrado"));
        registroCitaMedicaRepository.delete(registro);
    }

    private RegistroCitaMedicaResponse toResponse(RegistroCitaMedica registro) {
        return new RegistroCitaMedicaResponse(
                registro.getId(),
                registro.getHistoriaClinica().getId(),
                registro.getCita().getId(),
                registro.getNotasMedicas()
        );
    }
}

