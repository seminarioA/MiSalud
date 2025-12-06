package com.medx.beta.dto;

import com.medx.beta.model.Tratamiento;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TratamientoRequest(
        @NotNull Long diagnosticoId,
        @NotNull String descripcion,
        @NotNull LocalDate fechaInicio,
        LocalDate fechaFin,
        @NotNull Tratamiento.EstadoTratamiento estado) {
}
