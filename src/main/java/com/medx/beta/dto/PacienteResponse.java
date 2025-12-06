package com.medx.beta.dto;

import com.medx.beta.model.Paciente;

public record PacienteResponse(
                Long id,
                PersonaResponse persona,
                Long seguroId,
                String nombreSeguro) {
}
