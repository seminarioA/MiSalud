package com.medx.beta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResponsablePacienteRequest(
        @NotNull Long pacienteId,
        @NotNull Long personaResponsableId,
        @NotBlank @Size(max = 50) String relacion
) {}

