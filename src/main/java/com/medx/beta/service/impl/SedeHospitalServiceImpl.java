package com.medx.beta.service.impl;

import com.medx.beta.dto.SedeHospitalRequest;
import com.medx.beta.dto.SedeHospitalResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Hospital;
import com.medx.beta.model.SedeHospital;
import com.medx.beta.repository.HospitalRepository;
import com.medx.beta.repository.SedeHospitalRepository;
import com.medx.beta.service.SedeHospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SedeHospitalServiceImpl implements SedeHospitalService {

    private final SedeHospitalRepository sedeHospitalRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SedeHospitalResponse> getAll() {
        return sedeHospitalRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SedeHospitalResponse getById(Integer id) {
        SedeHospital sede = sedeHospitalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sede hospital no encontrada con id: " + id));
        return toResponse(sede);
    }

    @Override
    public SedeHospitalResponse create(SedeHospitalRequest request) {
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new NotFoundException("Hospital no encontrado con id: " + request.getHospitalId()));
        SedeHospital sede = new SedeHospital();
        sede.setSede(request.getSede());
        sede.setUbicacion(request.getUbicacion());
        sede.setHospital(hospital);
        SedeHospital saved = sedeHospitalRepository.save(sede);
        return toResponse(saved);
    }

    @Override
    public SedeHospitalResponse update(Integer id, SedeHospitalRequest request) {
        SedeHospital existente = sedeHospitalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sede hospital no encontrada con id: " + id));
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new NotFoundException("Hospital no encontrado con id: " + request.getHospitalId()));
        existente.setSede(request.getSede());
        existente.setUbicacion(request.getUbicacion());
        existente.setHospital(hospital);
        SedeHospital saved = sedeHospitalRepository.save(existente);
        return toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        SedeHospital existente = sedeHospitalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sede hospital no encontrada con id: " + id));
        sedeHospitalRepository.delete(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SedeHospitalResponse> getByHospital(Integer hospitalId) {
        return sedeHospitalRepository.findByHospital_HospitalId(hospitalId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private SedeHospitalResponse toResponse(SedeHospital sede) {
        SedeHospitalResponse dto = new SedeHospitalResponse();
        dto.setSedeId(sede.getSedeId());
        dto.setSede(sede.getSede());
        dto.setUbicacion(sede.getUbicacion());
        dto.setHospitalId(sede.getHospital() != null ? sede.getHospital().getHospitalId() : null);
        dto.setFechaCreacion(sede.getFechaCreacion());
        dto.setFechaActualizacion(sede.getFechaActualizacion());
        return dto;
    }
}

