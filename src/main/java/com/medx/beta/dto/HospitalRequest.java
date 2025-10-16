package com.medx.beta.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class HospitalRequest {
    @NotBlank
    private String nombre;
    private String descripcion;
}

