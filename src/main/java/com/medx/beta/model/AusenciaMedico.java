package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ausencias_medicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AusenciaMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ausencia")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_doctor", nullable = false, foreignKey = @ForeignKey(name = "fk_ausencias_medicos__doctores"))
    private Doctor doctor;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio; // Null means full day

    @Column(name = "hora_fin")
    private LocalTime horaFin; // Null means full day

    @Column(name = "motivo", nullable = false, length = 100)
    private String motivo;
}
