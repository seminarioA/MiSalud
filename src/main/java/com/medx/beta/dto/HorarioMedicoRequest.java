package com.medx.beta.dto;

import com.medx.beta.model.HorarioMedico;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HorarioMedicoRequest(
        @NotNull Long doctorId,
        @NotNull HorarioMedico.DiaSemana diaSemana,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFin,
        @NotNull Boolean esVacaciones
) {}

