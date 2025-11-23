package com.medx.beta.service;

import com.medx.beta.dto.HorarioMedicoRequest;
import com.medx.beta.dto.HorarioMedicoResponse;

import java.util.List;

public interface HorarioMedicoService {

    List<HorarioMedicoResponse> findAll();

    HorarioMedicoResponse findById(Long id);

    HorarioMedicoResponse create(HorarioMedicoRequest request);

    HorarioMedicoResponse update(Long id, HorarioMedicoRequest request);

    void delete(Long id);

    List<HorarioMedicoResponse> findByDoctor(Long doctorId);
}

