package com.medx.beta.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PacienteRequest(
        @Valid @NotNull PersonaRequest persona,
        @NotNull Long seguroId) {
}
