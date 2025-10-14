package com.medx.beta.dto;

import com.medx.beta.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private Long usuarioId;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private Usuario.Role rol;

    public AuthResponse(String token, Long usuarioId, String username, String email, 
                       String nombre, String apellido, Usuario.Role rol) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.username = username;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
    }
}