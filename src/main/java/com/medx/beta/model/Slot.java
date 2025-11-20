package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "Slot", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sede_id", "fecha", "hora_inicio", "hora_fin"}, name = "uk_slot_sede_fecha_hora")
})
@Getter
@Setter
public class Slot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @NotNull
    @Column(nullable = false)
    private java.time.LocalDate fecha;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private java.time.LocalTime horaInicio;

    @NotNull
    @Column(name = "hora_fin", nullable = false)
    private java.time.LocalTime horaFin;

    @NotNull
    @Column(nullable = false)
    private Boolean disponible = true;

    @NotNull(message = "La sede es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sede_id", nullable = false, foreignKey = @ForeignKey(name = "fk_slot_sede"))
    private SedeHospital sede;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", foreignKey = @ForeignKey(name = "fk_slot_cita"))
    private CitaMedica cita;

    @PrePersist
    @PreUpdate
    private void validarHoras() {
        if (horaInicio != null && horaFin != null && !horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin");
        }
    }
}
