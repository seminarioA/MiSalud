package com.medx.beta.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class FacturacionRequest {
    @NotNull(message = "La cita es obligatoria")
    private Long citaId;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal montoTotal;

    @NotNull(message = "El monto pagado es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal montoPagado;

    @NotBlank(message = "El tipo de pago es obligatorio")
    private String tipoPago;

    @NotBlank(message = "El estado de pago es obligatorio")
    private String estadoPago;
}

