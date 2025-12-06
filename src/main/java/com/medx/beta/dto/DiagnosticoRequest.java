package com.medx.beta.dto;

import com.medx.beta.model.Diagnostico;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DiagnosticoRequest(
                @NotNull Long citaId,
                String cie10,
                @NotNull String descripcion,
                @NotNull Diagnostico.TipoDiagnostico tipo,
                LocalDateTime fechaDiagnostico) {
}
