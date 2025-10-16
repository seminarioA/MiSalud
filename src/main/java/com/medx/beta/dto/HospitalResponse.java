package com.medx.beta.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class HospitalResponse {
    private Integer hospitalId;
    private String nombre;
    private String descripcion;
    private String fechaCreacion;
}

