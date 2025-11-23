package com.medx.beta.service;

import com.medx.beta.dto.EspecialidadRequest;
import com.medx.beta.dto.EspecialidadResponse;

import java.util.List;

public interface EspecialidadService {

    List<EspecialidadResponse> findAll();

    EspecialidadResponse findById(Long id);

    EspecialidadResponse create(EspecialidadRequest request);

    EspecialidadResponse update(Long id, EspecialidadRequest request);

    void delete(Long id);
}

