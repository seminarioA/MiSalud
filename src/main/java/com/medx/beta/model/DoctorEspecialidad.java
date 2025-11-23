package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_especialidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorEspecialidad {

    @EmbeddedId
    private DoctorEspecialidadId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("doctorId")
    @JoinColumn(name = "id_doctor", nullable = false,
            foreignKey = @ForeignKey(name = "fk_doctor_especialidades__doctores"))
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("especialidadId")
    @JoinColumn(name = "id_especialidad", nullable = false,
            foreignKey = @ForeignKey(name = "fk_doctor_especialidades__especialidades"))
    private Especialidad especialidad;
}

