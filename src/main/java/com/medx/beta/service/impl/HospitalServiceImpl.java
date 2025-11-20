package com.medx.beta.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medx.beta.dto.HospitalRequest;
import com.medx.beta.dto.HospitalResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Hospital;
import com.medx.beta.repository.HospitalRepository;
import com.medx.beta.service.HospitalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HospitalResponse> getAll() {
        return hospitalRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HospitalResponse getById(Integer id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hospital no encontrado con id: " + id));
        return toResponse(hospital);
    }

    @Override
    public HospitalResponse create(HospitalRequest hospitalRequest) {
        Hospital hospital = new Hospital();
        hospital.setNombre(hospitalRequest.getNombre());
        hospital.setDescripcion(hospitalRequest.getDescripcion());
        Hospital saved = hospitalRepository.save(hospital);
        return toResponse(saved);
    }

    @Override
    public HospitalResponse update(Integer id, HospitalRequest hospitalRequest) {
        Hospital existente = hospitalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hospital no encontrado con id: " + id));
        existente.setNombre(hospitalRequest.getNombre());
        existente.setDescripcion(hospitalRequest.getDescripcion());
        Hospital saved = hospitalRepository.save(existente);
        return toResponse(saved);
    }

    @Override
    public void deleteById(Integer id) {
        hospitalRepository.deleteById(id);
    }

    private HospitalResponse toResponse(Hospital hospital) {
        HospitalResponse dto = new HospitalResponse();
        dto.setHospitalId(hospital.getHospitalId());
        dto.setNombre(hospital.getNombre());
        dto.setDescripcion(hospital.getDescripcion());
        dto.setFechaCreacion(hospital.getFechaCreacion());
        dto.setFechaActualizacion(hospital.getFechaActualizacion());
        return dto;
    }
}
