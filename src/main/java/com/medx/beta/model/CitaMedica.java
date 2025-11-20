package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "Cita_Medica",
        uniqueConstraints = @UniqueConstraint(name = "uq_cita__doctor_fecha", columnNames = {"doctor_role_id", "fecha"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_role_id", foreignKey = @ForeignKey(name = "fk_cita__doctor_role"))
    private RoleDoctor doctor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_role_id", foreignKey = @ForeignKey(name = "fk_cita__patient_role"))
    private RolePatient paciente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Integer duracionMinutos = 30;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Estado estado = Estado.PROGRAMADA;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costo = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "dia")
    private LocalDate dia;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Estado {
        PROGRAMADA, EN_CURSO, CANCELADA, COMPLETADA
    }
}
