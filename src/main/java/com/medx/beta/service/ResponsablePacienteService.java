package com.medx.beta.service;

import com.medx.beta.dto.ResponsablePacienteRequest;
import com.medx.beta.dto.ResponsablePacienteResponse;

import java.util.List;

public interface ResponsablePacienteService {

    List<ResponsablePacienteResponse> findAll();

    ResponsablePacienteResponse findById(Long id);

    ResponsablePacienteResponse create(ResponsablePacienteRequest request);

    ResponsablePacienteResponse update(Long id, ResponsablePacienteRequest request);

    void delete(Long id);

    List<ResponsablePacienteResponse> findByPaciente(Long pacienteId);
}

