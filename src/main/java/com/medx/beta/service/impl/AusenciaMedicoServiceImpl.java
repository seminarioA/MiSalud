package com.medx.beta.service.impl;

import com.medx.beta.dto.AusenciaMedicoRequest;
import com.medx.beta.dto.AusenciaMedicoResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.AusenciaMedico;
import com.medx.beta.model.Doctor;
import com.medx.beta.repository.AusenciaMedicoRepository;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.service.AusenciaMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AusenciaMedicoServiceImpl implements AusenciaMedicoService {

    private final AusenciaMedicoRepository ausenciaRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public AusenciaMedicoResponse create(AusenciaMedicoRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));

        AusenciaMedico ausencia = AusenciaMedico.builder()
                .doctor(doctor)
                .fechaInicio(request.fechaInicio())
                .fechaFin(request.fechaFin())
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .motivo(request.motivo())
                .build();

        ausencia = ausenciaRepository.save(ausencia);
        return mapToResponse(ausencia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AusenciaMedicoResponse> findByDoctorId(Long doctorId) {
        return ausenciaRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!ausenciaRepository.existsById(id)) {
            throw new NotFoundException("Ausencia no encontrada");
        }
        ausenciaRepository.deleteById(id);
    }

    private AusenciaMedicoResponse mapToResponse(AusenciaMedico a) {
        return new AusenciaMedicoResponse(
                a.getId(),
                a.getDoctor().getId(),
                a.getFechaInicio(),
                a.getFechaFin(),
                a.getHoraInicio(),
                a.getHoraFin(),
                a.getMotivo());
    }
}
