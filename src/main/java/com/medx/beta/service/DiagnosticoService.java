package com.medx.beta.service;

import com.medx.beta.dto.DiagnosticoRequest;
import com.medx.beta.dto.DiagnosticoResponse;

import java.util.List;

public interface DiagnosticoService {

    List<DiagnosticoResponse> findAll();

    DiagnosticoResponse findById(Long id);

    DiagnosticoResponse create(DiagnosticoRequest request);

    DiagnosticoResponse update(Long id, DiagnosticoRequest request);

    void delete(Long id);
}

