package com.medx.beta.service;

import com.medx.beta.dto.CitaMedicaRequest;
import com.medx.beta.dto.CitaMedicaResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaMedicaService {
    List<CitaMedicaResponse> getAll();
    CitaMedicaResponse getById(Integer id);
    CitaMedicaResponse create(CitaMedicaRequest citaMedicaRequest);
    CitaMedicaResponse update(Integer id, CitaMedicaRequest citaMedicaRequest);
    void deleteById(Integer id);

    List<CitaMedicaResponse> getByDoctor(Integer doctorId);
    List<CitaMedicaResponse> getByPaciente(Integer pacienteId);
    List<CitaMedicaResponse> getByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    boolean isOwner(Integer citaId, Integer pacienteId);
}
