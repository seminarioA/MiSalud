package com.medx.beta.dto;

import com.medx.beta.model.CitaMedica;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CitaMedicaRequest {
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotNull(message = "El tipo de cita es obligatorio")
    private CitaMedica.TipoCita tipoCita;

    @NotNull(message = "El estado de la cita es obligatorio")
    private CitaMedica.EstadoCita estado;

    @NotNull(message = "El costo es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal costo;

    @NotNull(message = "El doctor es obligatorio")
    private Integer doctorId;

    @NotNull(message = "El paciente es obligatorio")
    private Integer pacienteId;
}

