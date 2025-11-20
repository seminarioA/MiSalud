package com.medx.beta.dto;

import com.medx.beta.model.CitaMedica;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CitaMedicaResponse {
    private Integer citaId;
    private LocalDateTime fecha;
    private CitaMedica.TipoCita tipoCita;
    private CitaMedica.EstadoCita estado;
    private BigDecimal costo;
    private Integer doctorId;
    private Integer pacienteId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}

