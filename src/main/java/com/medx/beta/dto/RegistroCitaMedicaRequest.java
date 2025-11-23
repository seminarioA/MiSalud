package com.medx.beta.dto;

import jakarta.validation.constraints.NotNull;

public record RegistroCitaMedicaRequest(
        @NotNull Long historiaClinicaId,
        @NotNull Long citaId,
        String notasMedicas
) {}

