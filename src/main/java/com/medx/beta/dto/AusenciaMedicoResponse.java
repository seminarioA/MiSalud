package com.medx.beta.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AusenciaMedicoResponse(
        Long id,
        Long doctorId,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        LocalTime horaInicio,
        LocalTime horaFin,
        String motivo) {
}
