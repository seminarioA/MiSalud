package com.medx.beta.dto;

import com.medx.beta.model.Paciente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PacienteRequest(
        @Valid @NotNull PersonaRequest persona,
        @NotNull Paciente.TipoSeguro tipoSeguro
) {}

