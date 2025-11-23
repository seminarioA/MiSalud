package com.medx.beta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DoctorEspecialidadId implements Serializable {

    @Column(name = "id_doctor")
    private Long doctorId;

    @Column(name = "id_especialidad")
    private Long especialidadId;
}

