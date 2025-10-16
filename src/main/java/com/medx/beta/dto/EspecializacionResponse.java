package com.medx.beta.dto;

public class EspecializacionResponse {
    private Integer especializacionId;
    private String nombre;
    private String descripcion;

    public EspecializacionResponse() {}

    public EspecializacionResponse(Integer especializacionId, String nombre, String descripcion) {
        this.especializacionId = especializacionId;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getEspecializacionId() {
        return especializacionId;
    }

    public void setEspecializacionId(Integer especializacionId) {
        this.especializacionId = especializacionId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

