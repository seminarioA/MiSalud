package com.medx.beta.service;

import com.medx.beta.dto.RegistroCitaMedicaRequest;
import com.medx.beta.dto.RegistroCitaMedicaResponse;

import java.util.List;

public interface RegistroCitaMedicaService {

    List<RegistroCitaMedicaResponse> findAll();

    RegistroCitaMedicaResponse findById(Long id);

    RegistroCitaMedicaResponse create(RegistroCitaMedicaRequest request);

    RegistroCitaMedicaResponse update(Long id, RegistroCitaMedicaRequest request);

    void delete(Long id);
}

