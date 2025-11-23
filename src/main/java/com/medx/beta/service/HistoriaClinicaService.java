package com.medx.beta.service;

import com.medx.beta.dto.HistoriaClinicaRequest;
import com.medx.beta.dto.HistoriaClinicaResponse;

import java.util.List;

public interface HistoriaClinicaService {

    List<HistoriaClinicaResponse> findAll();

    HistoriaClinicaResponse findById(Long id);

    HistoriaClinicaResponse create(HistoriaClinicaRequest request);

    HistoriaClinicaResponse update(Long id, HistoriaClinicaRequest request);

    void delete(Long id);

    HistoriaClinicaResponse findByPaciente(Long pacienteId);
}

