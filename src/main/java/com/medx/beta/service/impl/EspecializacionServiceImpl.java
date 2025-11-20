package com.medx.beta.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medx.beta.dto.EspecializacionRequest;
import com.medx.beta.dto.EspecializacionResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Especializacion;
import com.medx.beta.repository.EspecializacionRepository;
import com.medx.beta.service.EspecializacionService;

@Service
public class EspecializacionServiceImpl implements EspecializacionService {
    @Autowired
    private EspecializacionRepository especializacionRepository;

    @Override
    public List<EspecializacionResponse> getAll() {
        return especializacionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public EspecializacionResponse getById(Integer id) {
        Especializacion especializacion = especializacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Especialización no encontrada con ID: " + id));
        return toResponse(especializacion);
    }

    @Override
    public EspecializacionResponse create(EspecializacionRequest request) {
        Especializacion especializacion = new Especializacion();
        especializacion.setNombre(request.getNombre());
        especializacion.setDescripcion(request.getDescripcion());
        Especializacion saved = especializacionRepository.save(especializacion);
        return toResponse(saved);
    }

    @Override
    public EspecializacionResponse update(Integer id, EspecializacionRequest request) {
        Especializacion existente = especializacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Especialización no encontrada con ID: " + id));
        existente.setNombre(request.getNombre());
        existente.setDescripcion(request.getDescripcion());
        Especializacion saved = especializacionRepository.save(existente);
        return toResponse(saved);
    }

    @Override
    public void deleteById(Integer id) {
        Especializacion existente = especializacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Especialización no encontrada con ID: " + id));
        especializacionRepository.delete(existente);
    }
    
    public boolean existsByNombre(String nombre) {
        return especializacionRepository.existsByNombreIgnoreCase(nombre);
    }
    
    public boolean existsByNombreAndNotId(String nombre, Integer especializacionId) {
        return especializacionRepository.existsByNombreIgnoreCaseAndEspecializacionIdNot(nombre, especializacionId);
    }

    private EspecializacionResponse toResponse(Especializacion especializacion) {
        EspecializacionResponse dto = new EspecializacionResponse();
        dto.setEspecializacionId(especializacion.getEspecializacionId());
        dto.setNombre(especializacion.getNombre());
        dto.setDescripcion(especializacion.getDescripcion());
        return dto;
    }
}