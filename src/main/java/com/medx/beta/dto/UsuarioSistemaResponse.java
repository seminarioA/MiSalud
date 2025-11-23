package com.medx.beta.dto;

import com.medx.beta.model.UsuarioSistema;

public record UsuarioSistemaResponse(
        Long id,
        PersonaResponse persona,
        String email,
        UsuarioSistema.Rol rol
) {}

