package com.medx.beta.dto;

import com.medx.beta.model.Pago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoRequest(
        @NotNull Long citaId,
        @NotNull @DecimalMin("0.00") BigDecimal monto,
        @NotNull Pago.TipoTransaccion tipoTransaccion,
        @NotNull Pago.EstadoPago estadoPago,
        @NotNull Pago.MetodoPago metodoPago,
        String codigoOperacion,
        LocalDateTime fechaPago
) {}

