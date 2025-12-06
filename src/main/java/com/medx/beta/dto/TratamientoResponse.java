package com.medx.beta.dto;

import com.medx.beta.model.Tratamiento;
import java.time.LocalDate;

public record TratamientoResponse(
        Long id,
        Long diagnosticoId,
        String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Tratamiento.EstadoTratamiento estado) {
}
