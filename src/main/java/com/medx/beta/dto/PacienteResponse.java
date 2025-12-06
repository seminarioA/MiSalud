package com.medx.beta.dto;

public record PacienteResponse(
        Long id,
        PersonaResponse persona,
        Long seguroId,
        String nombreSeguro) {
}
