package com.medx.beta.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record AusenciaMedicoRequest(
        @NotNull Long doctorId,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        LocalTime horaInicio,
        LocalTime horaFin,
        @NotNull String motivo) {
}
