package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cita_Medica",
        indexes = { @Index(name = "idx_fecha", columnList = "fecha") })

@Getter
@Setter
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer citaId;

    @NotNull(message = "La fecha de la cita es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fecha;

    @NotNull(message = "El costo es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El costo no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El costo debe tener hasta 8 enteros y 2 decimales")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costo = BigDecimal.ZERO;

    @NotNull(message = "El doctor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Doctor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_Cita_Medica_Doctor1"))
    private Doctor doctor;

    @NotNull(message = "El paciente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Paciente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_Cita_Medica_Paciente1"))
    private Paciente paciente;

    public enum TipoCita {
        PRESENCIAL, TELEMEDICINA
    }

    public enum EstadoCita {
        Reservada, Confirmada, Cancelada, Completada, No_Asistio, Reprogramada
    }

    @NotNull(message = "El tipo de cita es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cita", nullable = false, length = 20)
    private TipoCita tipoCita;

    @NotNull(message = "El estado de la cita es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCita estado = EstadoCita.Reservada;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private java.time.LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private java.time.LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = java.time.LocalDateTime.now();
        this.fechaActualizacion = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = java.time.LocalDateTime.now();
    }
}
