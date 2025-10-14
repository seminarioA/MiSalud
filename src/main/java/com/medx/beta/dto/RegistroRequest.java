package com.medx.beta.dto;

import com.medx.beta.validation.ValidPassword;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistroRequest {
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El nombre de usuario solo puede contener letras, números y guiones bajos")
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no debe exceder 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @ValidPassword
    private String password;

    @NotBlank(message = "Confirmar contraseña es obligatorio")
    private String confirmarPassword;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 75, message = "El nombre no debe exceder 75 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 75, message = "El apellido no debe exceder 75 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El apellido solo puede contener letras y espacios")
    private String apellido;
}