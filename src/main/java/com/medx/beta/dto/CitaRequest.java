package com.medx.beta.dto;

import com.medx.beta.model.Cita;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record CitaRequest(
                Long pacienteId,
                @NotNull Long doctorId,
                @NotNull Long consultorioId,
                @NotNull LocalDate fechaCita,
                @NotNull LocalTime horaCita,
                @NotNull Integer duracionMinutos,
                @NotNull Cita.EstadoCita estado,
                @NotNull Cita.TipoAtencion tipoAtencion,
                @NotNull @DecimalMin("0.00") BigDecimal precioBase,
                @NotNull @DecimalMin("0.00") BigDecimal montoDescuento,
                Long seguroId) {
}
