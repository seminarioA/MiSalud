package com.medx.beta.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class HorarioBaseRequest {
    @NotBlank(message = "El día de la semana es obligatorio")
    private String diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private java.time.LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private java.time.LocalTime horaFin;
}

