package com.medx.beta.dto;

import jakarta.validation.constraints.NotNull;

public record HistoriaClinicaRequest(
        @NotNull Long pacienteId
) {}

