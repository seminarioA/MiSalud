package com.medx.beta.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FacturacionResponse {
    private Long facturacionId;
    private Long citaId;
    private BigDecimal montoTotal;
    private BigDecimal montoPagado;
    private String tipoPago;
    private String estadoPago;
    private LocalDateTime fechaEmision;
    private LocalDateTime fechaActualizacion;
}

