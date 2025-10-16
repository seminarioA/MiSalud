package com.medx.beta.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class SedeHospitalResponse {
    private Integer sedeId;
    private String sede;
    private String ubicacion;
    private Integer hospitalId;
    private String fechaCreacion;
}

