package com.medx.beta.dto;

import com.medx.beta.model.Cita;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CitaRequest(
                @NotNull Long pacienteId,
                @NotNull Long doctorId,
                @NotNull Long consultorioId,
                @NotNull LocalDate fechaCita,
                @NotNull LocalTime horaCita,
                @NotNull Integer duracionMinutos,
                @NotNull Cita.EstadoCita estado,
                @NotNull Cita.TipoAtencion tipoAtencion,
                LocalDateTime fechaImpresion,
                @NotNull @DecimalMin("0.00") BigDecimal precioBase,
                @NotNull @DecimalMin("0.00") BigDecimal montoDescuento,

                BigDecimal costoNetoCita, // Made optional as it can be calculated
                Long seguroId) {
}
