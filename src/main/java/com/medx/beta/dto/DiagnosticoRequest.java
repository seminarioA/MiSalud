package com.medx.beta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DiagnosticoRequest(
        @NotNull Long registroId,
        @NotBlank @Size(max = 10) String codigoIcd10,
        @Size(max = 255) String descripcion
) {}

