package com.medx.beta.dto;

import com.medx.beta.model.Persona;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PersonaRequest(
        @NotBlank @Size(max = 50) String primerNombre,
        @Size(max = 50) String segundoNombre,
        @NotBlank @Size(max = 50) String primerApellido,
        @Size(max = 50) String segundoApellido,
        @NotNull Persona.TipoDocumento tipoDocumento,
        @NotBlank @Size(max = 20) String numeroDocumento,
        @NotNull LocalDate fechaNacimiento,
        @NotNull Persona.Genero genero,
        @Size(max = 20) String numeroTelefono,
        @Size(max = 255) String urlFotoPerfil
) {}

