package com.medx.beta.dto;

import com.medx.beta.model.Diagnostico;
import java.time.LocalDateTime;

public record DiagnosticoResponse(
                Long id,
                Long citaId,
                Long historiaClinicaId,
                String cie10,
                String descripcion,
                Diagnostico.TipoDiagnostico tipo,
                LocalDateTime fechaDiagnostico) {
}
