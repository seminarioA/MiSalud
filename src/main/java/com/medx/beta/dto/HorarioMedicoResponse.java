package com.medx.beta.dto;

import com.medx.beta.model.HorarioMedico;

import java.time.LocalTime;

public record HorarioMedicoResponse(
        Long id,
        Long doctorId,
        HorarioMedico.DiaSemana diaSemana,
        LocalTime horaInicio,
        LocalTime horaFin,
        Boolean esVacaciones
) {}

