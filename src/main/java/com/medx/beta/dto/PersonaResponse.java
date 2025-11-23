package com.medx.beta.dto;

import com.medx.beta.model.Persona;

import java.time.LocalDate;

public record PersonaResponse(
        Long id,
        String primerNombre,
        String segundoNombre,
        String primerApellido,
        String segundoApellido,
        Persona.TipoDocumento tipoDocumento,
        String numeroDocumento,
        LocalDate fechaNacimiento,
        Persona.Genero genero,
        String numeroTelefono,
        String urlFotoPerfil
) {}

