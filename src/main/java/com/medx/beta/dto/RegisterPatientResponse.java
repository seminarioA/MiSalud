package com.medx.beta.dto;

public record RegisterPatientResponse(
        AuthResponse auth,
        PacienteResponse paciente
) {
}

