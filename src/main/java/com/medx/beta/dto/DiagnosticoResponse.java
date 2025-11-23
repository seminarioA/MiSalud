package com.medx.beta.dto;

public record DiagnosticoResponse(
        Long id,
        Long registroId,
        String codigoIcd10,
        String descripcion
) {}

