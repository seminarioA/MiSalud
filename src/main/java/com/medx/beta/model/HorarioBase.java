package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Horario_Base")
@Getter
@Setter
public class HorarioBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer turnoId;

    public enum DiaSemana {
        Lunes, Martes, Miercoles, Jueves, Viernes, Sabado, Domingo
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false, length = 15)
    private DiaSemana diaSemana;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private java.time.LocalTime horaInicio;

    @NotNull
    @Column(name = "hora_fin", nullable = false)
    private java.time.LocalTime horaFin;

    @PrePersist
    @PreUpdate
    private void validarHoras() {
        if (horaInicio != null && horaFin != null && !horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin");
        }
    }
}

