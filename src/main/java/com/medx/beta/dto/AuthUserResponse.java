package com.medx.beta.dto;

import com.medx.beta.model.Usuario;

public record AuthUserResponse(
        Long usuarioId,
        String username,
        String email,
        String nombre,
        String apellido,
        Usuario.Role rol,
        Boolean estaActivo
) {}
