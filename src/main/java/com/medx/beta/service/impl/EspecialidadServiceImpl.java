package com.medx.beta.service.impl;

import com.medx.beta.dto.EspecialidadRequest;
import com.medx.beta.dto.EspecialidadResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Especialidad;
import com.medx.beta.repository.EspecialidadRepository;
import com.medx.beta.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadResponse> findAll() {
        return especialidadRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EspecialidadResponse findById(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Especialidad no encontrada"));
        return toResponse(especialidad);
    }

    @Override
    public EspecialidadResponse create(EspecialidadRequest request) {
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(request.nombre());
        return toResponse(especialidadRepository.save(especialidad));
    }

    @Override
    public EspecialidadResponse update(Long id, EspecialidadRequest request) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Especialidad no encontrada"));
        especialidad.setNombre(request.nombre());
        return toResponse(especialidadRepository.save(especialidad));
    }

    @Override
    public void delete(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Especialidad no encontrada"));
        especialidadRepository.delete(especialidad);
    }

    private EspecialidadResponse toResponse(Especialidad especialidad) {
        return new EspecialidadResponse(especialidad.getId(), especialidad.getNombre());
    }
}

