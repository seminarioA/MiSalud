package com.medx.beta.service;

import com.medx.beta.dto.CitaRequest;
import com.medx.beta.dto.CitaResponse;

import java.time.LocalDate;
import java.util.List;

public interface CitaService {

    List<CitaResponse> findAll();

    CitaResponse findById(Long id);

    CitaResponse create(CitaRequest request);

    CitaResponse update(Long id, CitaRequest request);

    void delete(Long id);

    List<CitaResponse> findByDoctor(Long doctorId, LocalDate fecha);

    List<CitaResponse> findByPaciente(Long pacienteId);
}

