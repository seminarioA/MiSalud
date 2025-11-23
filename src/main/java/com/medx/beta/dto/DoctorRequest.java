package com.medx.beta.dto;

import com.medx.beta.model.Doctor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DoctorRequest(
        @Valid @NotNull PersonaRequest persona,
        @Size(max = 20) String numeroColegiatura,
        @NotNull List<Long> especialidadIds
) {}

