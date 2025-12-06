package com.medx.beta.dto;

import com.medx.beta.model.Cita;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CitaResponse(
                Long id,
                Long pacienteId,
                Long doctorId,
                Long consultorioId,
                LocalDate fechaCita,
                LocalTime horaCita,
                Integer duracionMinutos,
                Cita.EstadoCita estado,
                Cita.TipoAtencion tipoAtencion,
                LocalDateTime fechaImpresion,
                BigDecimal precioBase,
                BigDecimal montoDescuento,

                BigDecimal costoNetoCita,
                Long seguroId,
                String nombreSeguro,
                BigDecimal copagoEstimado) {
}
