package com.medx.beta.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioBaseResponse {
    private Integer turnoId;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}

