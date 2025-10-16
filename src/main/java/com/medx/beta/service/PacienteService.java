package com.medx.beta.service;

import com.medx.beta.dto.PacienteRequest;
import com.medx.beta.dto.PacienteResponse;
import java.util.List;

public interface PacienteService {
    // Lista todos los pacientes
    List<PacienteResponse> getAll();
    // Busca un paciente por ID
    PacienteResponse getById(Integer id);
    // Crea un nuevo paciente
    PacienteResponse create(PacienteRequest pacienteRequest);
    // Actualiza un paciente existente
    PacienteResponse update(Integer id, PacienteRequest pacienteRequest);
    // Elimina (hard delete) un paciente por ID
    void deleteById(Integer id);
}
