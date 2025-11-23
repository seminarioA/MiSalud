package com.medx.beta.service;

import com.medx.beta.dto.PacienteRequest;
import com.medx.beta.dto.PacienteResponse;

import java.util.List;

public interface PacienteService {

    List<PacienteResponse> findAll();

    PacienteResponse findById(Long id);

    PacienteResponse create(PacienteRequest request);

    PacienteResponse update(Long id, PacienteRequest request);

    void delete(Long id);
}

