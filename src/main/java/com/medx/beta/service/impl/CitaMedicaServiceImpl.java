package com.medx.beta.service.impl;

import com.medx.beta.dto.CitaMedicaRequest;
import com.medx.beta.dto.CitaMedicaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.CitaMedica;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.Paciente;
import com.medx.beta.repository.CitaMedicaRepository;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.service.CitaMedicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaMedicaServiceImpl implements CitaMedicaService {

    private final CitaMedicaRepository citaMedicaRepository;
    private final DoctorRepository doctorRepository;
    private final PacienteRepository pacienteRepository;

    @Override
    public List<CitaMedicaResponse> getAll() {
        return citaMedicaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CitaMedicaResponse getById(Integer id) {
        CitaMedica cita = citaMedicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita médica no encontrada con id: " + id));
        return toResponse(cita);
    }

    @Override
    public CitaMedicaResponse create(CitaMedicaRequest citaMedicaRequest) {
        Doctor doctor = doctorRepository.findById(citaMedicaRequest.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado con id: " + citaMedicaRequest.getDoctorId()));
        Paciente paciente = pacienteRepository.findById(citaMedicaRequest.getPacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con id: " + citaMedicaRequest.getPacienteId()));
        CitaMedica cita = new CitaMedica();
        cita.setCitaId(null);
        cita.setFecha(citaMedicaRequest.getFecha());
        cita.setCosto(citaMedicaRequest.getCosto());
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        // Si tienes tipoCita y estado como enums, deberías mapearlos aquí
        // cita.setTipoCita(CitaMedica.TipoCita.valueOf(citaMedicaRequest.getTipoCita()));
        CitaMedica saved = citaMedicaRepository.save(cita);
        return toResponse(saved);
    }

    @Override
    public CitaMedicaResponse update(Integer id, CitaMedicaRequest citaMedicaRequest) {
        CitaMedica existente = citaMedicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita médica no encontrada con id: " + id));
        Doctor doctor = doctorRepository.findById(citaMedicaRequest.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado con id: " + citaMedicaRequest.getDoctorId()));
        Paciente paciente = pacienteRepository.findById(citaMedicaRequest.getPacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con id: " + citaMedicaRequest.getPacienteId()));
        existente.setFecha(citaMedicaRequest.getFecha());
        existente.setCosto(citaMedicaRequest.getCosto());
        existente.setDoctor(doctor);
        existente.setPaciente(paciente);
        // existente.setTipoCita(CitaMedica.TipoCita.valueOf(citaMedicaRequest.getTipoCita()));
        CitaMedica saved = citaMedicaRepository.save(existente);
        return toResponse(saved);
    }

    @Override
    public void deleteById(Integer id) {
        CitaMedica existente = citaMedicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita médica no encontrada con id: " + id));
        citaMedicaRepository.delete(existente);
    }

    @Override
    public List<CitaMedicaResponse> getByDoctor(Integer doctorId) {
        return citaMedicaRepository.findByDoctor_DoctorId(doctorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CitaMedicaResponse> getByPaciente(Integer pacienteId) {
        return citaMedicaRepository.findByPaciente_PacienteId(pacienteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CitaMedicaResponse> getByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return citaMedicaRepository.findByFechaBetween(inicio, fin).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CitaMedicaResponse toResponse(CitaMedica cita) {
        CitaMedicaResponse dto = new CitaMedicaResponse();
        dto.setCitaId(cita.getCitaId() != null ? cita.getCitaId().longValue() : null);
        dto.setFecha(cita.getFecha());
        dto.setCosto(cita.getCosto());
        dto.setDoctorId(cita.getDoctor() != null ? cita.getDoctor().getDoctorId() : null);
        dto.setPacienteId(cita.getPaciente() != null ? cita.getPaciente().getPacienteId() : null);
        return dto;
    }
}
