package com.medx.beta.dto;

public record RegistroCitaMedicaResponse(
        Long id,
        Long historiaClinicaId,
        Long citaId,
        String notasMedicas
) {}

