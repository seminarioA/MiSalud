package com.medx.beta.dto;

import com.medx.beta.model.DocumentoFiscal;

public record DocumentoFiscalResponse(
        Long id,
        Long pagoId,
        DocumentoFiscal.TipoComprobante tipoComprobante,
        String serie,
        Integer correlativo,
        String rucCliente,
        String nombreClienteFiscal
) {}

