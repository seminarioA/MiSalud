package com.medx.beta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConsultorioRequest(
        @NotBlank @Size(max = 20) String nombreONumero
) {}

