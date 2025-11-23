package com.medx.beta.service.impl;

import com.medx.beta.dto.HorarioMedicoRequest;
import com.medx.beta.dto.HorarioMedicoResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.HorarioMedico;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.repository.HorarioMedicoRepository;
import com.medx.beta.service.HorarioMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HorarioMedicoServiceImpl implements HorarioMedicoService {

    private final HorarioMedicoRepository horarioMedicoRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HorarioMedicoResponse> findAll() {
        return horarioMedicoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioMedicoResponse findById(Long id) {
        HorarioMedico horario = horarioMedicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Horario no encontrado"));
        return toResponse(horario);
    }

    @Override
    public HorarioMedicoResponse create(HorarioMedicoRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
        HorarioMedico horario = new HorarioMedico();
        applyRequest(horario, request, doctor);
        return toResponse(horarioMedicoRepository.save(horario));
    }

    @Override
    public HorarioMedicoResponse update(Long id, HorarioMedicoRequest request) {
        HorarioMedico horario = horarioMedicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Horario no encontrado"));
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
        applyRequest(horario, request, doctor);
        return toResponse(horarioMedicoRepository.save(horario));
    }

    @Override
    public void delete(Long id) {
        HorarioMedico horario = horarioMedicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Horario no encontrado"));
        horarioMedicoRepository.delete(horario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HorarioMedicoResponse> findByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
        return horarioMedicoRepository.findAll().stream()
                .filter(h -> h.getDoctor().equals(doctor))
                .map(this::toResponse)
                .toList();
    }

    private void applyRequest(HorarioMedico horario, HorarioMedicoRequest request, Doctor doctor) {
        horario.setDoctor(doctor);
        horario.setDiaSemana(request.diaSemana());
        horario.setHoraInicio(request.horaInicio());
        horario.setHoraFin(request.horaFin());
        horario.setEsVacaciones(request.esVacaciones());
    }

    private HorarioMedicoResponse toResponse(HorarioMedico horario) {
        return new HorarioMedicoResponse(
                horario.getId(),
                horario.getDoctor().getId(),
                horario.getDiaSemana(),
                horario.getHoraInicio(),
                horario.getHoraFin(),
                horario.getEsVacaciones()
        );
    }
}

