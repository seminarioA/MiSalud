package com.medx.beta.dto;

import lombok.Data;
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
    private String fechaCreacion;
    private List<Integer> especializacionIds;
}

