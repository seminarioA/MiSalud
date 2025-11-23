package com.medx.beta.dto;

import com.medx.beta.model.Pago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoResponse(
        Long id,
        Long citaId,
        BigDecimal monto,
        Pago.TipoTransaccion tipoTransaccion,
        Pago.EstadoPago estadoPago,
        Pago.MetodoPago metodoPago,
        String codigoOperacion,
        LocalDateTime fechaPago
) {}

