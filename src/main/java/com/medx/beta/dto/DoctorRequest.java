package com.medx.beta.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
public class DoctorRequest {
    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 75)
    private String primerNombre;

    @Size(max = 75)
    private String segundoNombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 75)
    private String primerApellido;

    @NotBlank(message = "El segundo apellido es obligatorio")
    @Size(max = 75)
    private String segundoApellido;

    @NotNull(message = "La sede es obligatoria")
    private Integer sedeId;

    @NotNull(message = "El turno es obligatorio")
    private Integer turnoId;

    private List<Integer> especializacionIds;
}
