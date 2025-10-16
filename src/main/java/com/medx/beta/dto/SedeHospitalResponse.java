package com.medx.beta.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SedeHospitalResponse {
    private Integer sedeId;
    private String sede;
    private String ubicacion;
    private Integer hospitalId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}

