package com.medx.beta.service.impl;

import com.medx.beta.dto.DoctorRequest;
import com.medx.beta.dto.DoctorResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.Especializacion;
import com.medx.beta.model.HorarioBase;
import com.medx.beta.model.SedeHospital;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.repository.SedeHospitalRepository;
import com.medx.beta.repository.HorarioBaseRepository;
import com.medx.beta.repository.EspecializacionRepository;
import com.medx.beta.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SedeHospitalRepository sedeHospitalRepository;
    private final HorarioBaseRepository horarioBaseRepository;
    private final EspecializacionRepository especializacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAll() {
        return doctorRepository.findAllActivos().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getById(Integer id) {
        Doctor doctor = doctorRepository.findByDoctorIdAndEstaActivoTrue(id)
                .orElseThrow(() -> new NotFoundException("Doctor no activo con id: " + id));
        return toResponse(doctor);
    }

    @Override
    public DoctorResponse create(DoctorRequest doctorRequest) {
        SedeHospital sede = sedeHospitalRepository.findById(doctorRequest.getSedeId())
                .orElseThrow(() -> new NotFoundException("Sede no encontrada con id: " + doctorRequest.getSedeId()));
        HorarioBase turno = horarioBaseRepository.findById(doctorRequest.getTurnoId())
                .orElseThrow(() -> new NotFoundException("Turno no encontrado con id: " + doctorRequest.getTurnoId()));
        Doctor doctor = new Doctor();
        doctor.setPrimerNombre(doctorRequest.getPrimerNombre());
        doctor.setSegundoNombre(doctorRequest.getSegundoNombre());
        doctor.setPrimerApellido(doctorRequest.getPrimerApellido());
        doctor.setSegundoApellido(doctorRequest.getSegundoApellido());
        doctor.setSedeHospital(sede);
        doctor.setTurno(turno);
        List<Especializacion> especializaciones = resolveEspecializaciones(doctorRequest.getEspecializacionIds());
        if (especializaciones != null) {
            doctor.setEspecializaciones(especializaciones);
        }
        Doctor saved = doctorRepository.save(doctor);
        return toResponse(saved);
    }

    @Override
    public DoctorResponse update(Integer id, DoctorRequest doctorRequest) {
        Doctor existente = doctorRepository.findByDoctorIdAndEstaActivoTrue(id)
                .orElseThrow(() -> new NotFoundException("Doctor no activo con id: " + id));
        SedeHospital sede = sedeHospitalRepository.findById(doctorRequest.getSedeId())
                .orElseThrow(() -> new NotFoundException("Sede no encontrada con id: " + doctorRequest.getSedeId()));
        HorarioBase turno = horarioBaseRepository.findById(doctorRequest.getTurnoId())
                .orElseThrow(() -> new NotFoundException("Turno no encontrado con id: " + doctorRequest.getTurnoId()));
        existente.setPrimerNombre(doctorRequest.getPrimerNombre());
        existente.setSegundoNombre(doctorRequest.getSegundoNombre());
        existente.setPrimerApellido(doctorRequest.getPrimerApellido());
        existente.setSegundoApellido(doctorRequest.getSegundoApellido());
        existente.setSedeHospital(sede);
        existente.setTurno(turno);
        List<Especializacion> especializaciones = resolveEspecializaciones(doctorRequest.getEspecializacionIds());
        if (especializaciones != null) {
            existente.setEspecializaciones(especializaciones);
        }
        Doctor saved = doctorRepository.save(existente);
        return toResponse(saved);
    }

    @Override
    public void deleteById(Integer id) {
        Doctor existente = doctorRepository.findByDoctorIdAndEstaActivoTrue(id)
                .orElseThrow(() -> new NotFoundException("Doctor no activo con id: " + id));
        doctorRepository.desactivarPorId(existente.getDoctorId());
    }

    private DoctorResponse toResponse(Doctor doctor) {
        DoctorResponse dto = new DoctorResponse();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setPrimerNombre(doctor.getPrimerNombre());
        dto.setSegundoNombre(doctor.getSegundoNombre());
        dto.setPrimerApellido(doctor.getPrimerApellido());
        dto.setSegundoApellido(doctor.getSegundoApellido());
        dto.setSedeId(doctor.getSedeHospital() != null ? doctor.getSedeHospital().getSedeId() : null);
        dto.setTurnoId(doctor.getTurno() != null ? doctor.getTurno().getTurnoId() : null);
        dto.setEstaActivo(doctor.getEstaActivo());
        dto.setFechaCreacion(doctor.getFechaCreacion());
        dto.setFechaActualizacion(doctor.getFechaActualizacion());
        if (doctor.getEspecializaciones() != null) {
            dto.setEspecializacionIds(doctor.getEspecializaciones().stream()
                    .map(Especializacion::getEspecializacionId)
                    .collect(Collectors.toList()));
        } else {
            dto.setEspecializacionIds(Collections.emptyList());
        }
        return dto;
    }

    private List<Especializacion> resolveEspecializaciones(List<Integer> especializacionIds) {
        if (especializacionIds == null) {
            return null;
        }
        if (especializacionIds.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        List<Especializacion> especializaciones = especializacionRepository.findAllById(especializacionIds);
        Set<Integer> encontrados = especializaciones.stream()
                .map(Especializacion::getEspecializacionId)
                .collect(Collectors.toSet());
        List<Integer> faltantes = especializacionIds.stream()
                .filter(id -> !encontrados.contains(id))
                .distinct()
                .collect(Collectors.toList());
        if (!faltantes.isEmpty()) {
            throw new NotFoundException("Especialización no encontrada con id: " + faltantes.get(0));
        }
        return new java.util.ArrayList<>(especializaciones);
    }
}
