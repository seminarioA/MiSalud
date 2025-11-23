package com.medx.beta.dto;

public record ResponsablePacienteResponse(
        Long id,
        Long pacienteId,
        Long personaResponsableId,
        String relacion
) {}

