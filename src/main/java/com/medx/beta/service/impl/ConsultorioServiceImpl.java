package com.medx.beta.service.impl;

import com.medx.beta.dto.ConsultorioRequest;
import com.medx.beta.dto.ConsultorioResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Consultorio;
import com.medx.beta.repository.ConsultorioRepository;
import com.medx.beta.service.ConsultorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultorioServiceImpl implements ConsultorioService {

    private final ConsultorioRepository consultorioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ConsultorioResponse> findAll() {
        return consultorioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultorioResponse findById(Long id) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Consultorio no encontrado"));
        return toResponse(consultorio);
    }

    @Override
    public ConsultorioResponse create(ConsultorioRequest request) {
        Consultorio consultorio = new Consultorio();
        consultorio.setNombreONumero(request.nombreONumero());
        return toResponse(consultorioRepository.save(consultorio));
    }

    @Override
    public ConsultorioResponse update(Long id, ConsultorioRequest request) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Consultorio no encontrado"));
        consultorio.setNombreONumero(request.nombreONumero());
        return toResponse(consultorioRepository.save(consultorio));
    }

    @Override
    public void delete(Long id) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Consultorio no encontrado"));
        consultorioRepository.delete(consultorio);
    }

    private ConsultorioResponse toResponse(Consultorio consultorio) {
        return new ConsultorioResponse(consultorio.getId(), consultorio.getNombreONumero());
    }
}

