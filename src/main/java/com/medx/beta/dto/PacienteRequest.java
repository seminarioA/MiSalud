package com.medx.beta.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class PacienteRequest {
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

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private java.time.LocalDate fechaNacimiento;

    @Size(max = 500)
    private String domicilio;
}
