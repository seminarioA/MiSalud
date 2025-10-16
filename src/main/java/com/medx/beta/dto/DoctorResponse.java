package com.medx.beta.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class DoctorResponse {
    private Integer doctorId;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private Integer sedeId;
    private Integer turnoId;
    private Boolean estaActivo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<Integer> especializacionIds = Collections.emptyList();
}

