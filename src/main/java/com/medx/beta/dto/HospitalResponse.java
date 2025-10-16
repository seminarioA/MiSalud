package com.medx.beta.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HospitalResponse {
    private Integer hospitalId;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;
}

