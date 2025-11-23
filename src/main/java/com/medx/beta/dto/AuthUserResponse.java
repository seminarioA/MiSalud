package com.medx.beta.dto;

import com.medx.beta.model.UsuarioSistema;

public record AuthUserResponse(Long id, String email, UsuarioSistema.Rol rol) {
}

