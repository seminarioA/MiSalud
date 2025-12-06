package com.medx.beta.service;

import com.medx.beta.dto.DiagnosticoRequest;
import com.medx.beta.dto.DiagnosticoResponse;
import java.util.List;

public interface DiagnosticoService {
    DiagnosticoResponse create(DiagnosticoRequest request);

    DiagnosticoResponse findById(Long id);

    List<DiagnosticoResponse> findByCitaId(Long citaId);

    List<DiagnosticoResponse> findByHistoriaClinicaId(Long historiaId);

    void delete(Long id);
}
