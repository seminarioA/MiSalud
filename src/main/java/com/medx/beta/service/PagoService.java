package com.medx.beta.service;

import com.medx.beta.dto.PagoRequest;
import com.medx.beta.dto.PagoResponse;

import java.util.List;

public interface PagoService {

    List<PagoResponse> findAll();

    PagoResponse findById(Long id);

    PagoResponse create(PagoRequest request);

    PagoResponse update(Long id, PagoRequest request);

    void delete(Long id);

    List<PagoResponse> findByCita(Long citaId);
}

