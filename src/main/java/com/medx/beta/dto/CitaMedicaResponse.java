package com.medx.beta.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CitaMedicaResponse {
    private Long citaId;
    private LocalDateTime fecha;
    private String tipoCita;
    private String estado;
    private BigDecimal costo;
    private Integer doctorId;
    private Integer pacienteId;
    private String fechaCreacion;
    private String fechaActualizacion;
}

