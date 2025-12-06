package com.medx.beta.service;

import com.medx.beta.dto.TratamientoRequest;
import com.medx.beta.dto.TratamientoResponse;
import java.util.List;

public interface TratamientoService {
    TratamientoResponse create(TratamientoRequest request);

    TratamientoResponse findById(Long id);

    List<TratamientoResponse> findByDiagnosticoId(Long diagnosticoId);

    void delete(Long id);
}
