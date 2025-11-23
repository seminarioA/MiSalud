package com.medx.beta.service;

import com.medx.beta.dto.ConsultorioRequest;
import com.medx.beta.dto.ConsultorioResponse;

import java.util.List;

public interface ConsultorioService {

    List<ConsultorioResponse> findAll();

    ConsultorioResponse findById(Long id);

    ConsultorioResponse create(ConsultorioRequest request);

    ConsultorioResponse update(Long id, ConsultorioRequest request);

    void delete(Long id);
}

