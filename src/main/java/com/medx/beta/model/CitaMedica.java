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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Doctor_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_Cita_Medica_Doctor1"))
    private Doctor doctor;

    @NotNull(message = "El paciente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Paciente_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_Cita_Medica_Paciente1"))
    private Paciente paciente;
}
