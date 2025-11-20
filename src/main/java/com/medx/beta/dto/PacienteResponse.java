package com.medx.beta.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PacienteResponse {
    private Integer pacienteId;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private LocalDate fechaNacimiento;
    private String domicilio;
    private Boolean estaActivo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}

