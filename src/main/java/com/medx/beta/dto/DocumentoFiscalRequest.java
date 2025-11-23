package com.medx.beta.dto;

import com.medx.beta.model.DocumentoFiscal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DocumentoFiscalRequest(
        @NotNull Long pagoId,
        @NotNull DocumentoFiscal.TipoComprobante tipoComprobante,
        @NotBlank @Size(max = 4) String serie,
        @NotNull Integer correlativo,
        @Size(max = 11) String rucCliente,
        @NotBlank @Size(max = 100) String nombreClienteFiscal
) {}

