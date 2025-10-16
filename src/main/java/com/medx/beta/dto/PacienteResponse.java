package com.medx.beta.dto;

import lombok.Data;

@Data
public class PacienteResponse {
    private Integer pacienteId;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private java.time.LocalDate fechaNacimiento;
    private String domicilio;
    private Boolean estaActivo;
    private String fechaCreacion;
}

