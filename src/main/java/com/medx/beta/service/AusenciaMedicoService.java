package com.medx.beta.service;

import com.medx.beta.dto.AusenciaMedicoRequest;
import com.medx.beta.dto.AusenciaMedicoResponse;
import java.util.List;

public interface AusenciaMedicoService {
    AusenciaMedicoResponse create(AusenciaMedicoRequest request);

    List<AusenciaMedicoResponse> findByDoctorId(Long doctorId);

    void delete(Long id);
}
