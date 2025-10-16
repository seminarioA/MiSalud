package com.medx.beta.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class SedeHospitalRequest {
    @NotBlank
    private String sede;
    private String ubicacion;
    @NotNull
    private Integer hospitalId;
}

