package com.medx.beta.dto;

import java.util.List;

public record DoctorResponse(
        Long id,
        PersonaResponse persona,
        String numeroColegiatura,
        List<Long> especialidadIds,
        List<String> especialidades
) {}
