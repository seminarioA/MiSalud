package com.medx.beta.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterPatientRequest(
        @Valid @NotNull PersonaRequest persona,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8) String password,
        Long seguroId
) {
}

