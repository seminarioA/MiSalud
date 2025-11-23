package com.medx.beta.service.impl;

import com.medx.beta.dto.CitaRequest;
import com.medx.beta.dto.CitaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Cita;
import com.medx.beta.model.Consultorio;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.Paciente;
import com.medx.beta.repository.CitaRepository;
import com.medx.beta.repository.ConsultorioRepository;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultorioRepository consultorioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> findAll() {
        return citaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse findById(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        return toResponse(cita);
    }

    @Override
    public CitaResponse create(CitaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
        Consultorio consultorio = consultorioRepository.findById(request.consultorioId())
                .orElseThrow(() -> new NotFoundException("Consultorio no encontrado"));
        Cita cita = new Cita();
        applyRequest(cita, request, paciente, doctor, consultorio);
        return toResponse(citaRepository.save(cita));
    }

    @Override
    public CitaResponse update(Long id, CitaRequest request) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado"));
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
        Consultorio consultorio = consultorioRepository.findById(request.consultorioId())
                .orElseThrow(() -> new NotFoundException("Consultorio no encontrado"));
        applyRequest(cita, request, paciente, doctor, consultorio);
        return toResponse(citaRepository.save(cita));
    }

    @Override
    public void delete(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada"));
        citaRepository.delete(cita);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> findByDoctor(Long doctorId, LocalDate fecha) {
        return citaRepository.findAll().stream()
                .filter(c -> c.getDoctor().getId().equals(doctorId) && c.getFechaCita().equals(fecha))
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> findByPaciente(Long pacienteId) {
        return citaRepository.findAll().stream()
                .filter(c -> c.getPaciente().getId().equals(pacienteId))
                .map(this::toResponse)
                .toList();
    }

    private void applyRequest(Cita cita, CitaRequest request, Paciente paciente, Doctor doctor, Consultorio consultorio) {
        cita.setPaciente(paciente);
        cita.setDoctor(doctor);
        cita.setConsultorio(consultorio);
        cita.setFechaCita(request.fechaCita());
        cita.setHoraCita(request.horaCita());
        cita.setDuracionMinutos(request.duracionMinutos());
        cita.setEstado(request.estado());
        cita.setTipoAtencion(request.tipoAtencion());
        cita.setFechaImpresion(request.fechaImpresion());
        cita.setPrecioBase(request.precioBase());
        cita.setMontoDescuento(request.montoDescuento());
        cita.setCostoNetoCita(request.costoNetoCita());
    }

    private CitaResponse toResponse(Cita cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getPaciente().getId(),
                cita.getDoctor().getId(),
                cita.getConsultorio().getId(),
                cita.getFechaCita(),
                cita.getHoraCita(),
                cita.getDuracionMinutos(),
                cita.getEstado(),
                cita.getTipoAtencion(),
                cita.getFechaImpresion(),
                cita.getPrecioBase(),
                cita.getMontoDescuento(),
                cita.getCostoNetoCita()
        );
    }
}

