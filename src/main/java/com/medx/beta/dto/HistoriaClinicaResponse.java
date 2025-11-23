package com.medx.beta.dto;

import java.time.LocalDateTime;

public record HistoriaClinicaResponse(
        Long id,
        Long pacienteId,
        LocalDateTime fechaApertura
) {}

