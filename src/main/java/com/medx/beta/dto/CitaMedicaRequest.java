package com.medx.beta.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CitaMedicaRequest {
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotBlank(message = "El tipo de cita es obligatorio")
    private String tipoCita;

    @NotNull(message = "El costo es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal costo;

    @NotNull(message = "El doctor es obligatorio")
    private Integer doctorId;

    @NotNull(message = "El paciente es obligatorio")
    private Integer pacienteId;
}

