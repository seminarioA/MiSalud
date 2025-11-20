package com.medx.beta.dto;

public record AuthResponse(
        String token,
        String type,
        Long usuarioId,
        String username,
        String email,
        String nombre,
        String apellido,
        Usuario.Role rol
) {
    // Constructor auxiliar para mantener compatibilidad: asume type = "Bearer"
    public AuthResponse(String token, Long usuarioId, String username, String email,
                        String nombre, String apellido, Usuario.Role rol) {
        this(token, "Bearer", usuarioId, username, email, nombre, apellido, rol);
    }
}