package com.medx.beta.dto;

import com.medx.beta.model.UsuarioSistema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioSistemaRequest(
        @Valid @NotNull PersonaRequest persona,
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotNull UsuarioSistema.Rol rol
) {}

