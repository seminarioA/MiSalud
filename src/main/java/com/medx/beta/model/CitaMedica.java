package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "cita_medica",
        uniqueConstraints = @UniqueConstraint(name = "uq_cita__doctor_fecha", columnNames = {"doctor_id", "fecha"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer citaId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", foreignKey = @ForeignKey(name = "fk_cita__doctor"))
    private Doctor doctor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", foreignKey = @ForeignKey(name = "fk_cita__paciente"))
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    @Builder.Default
    private Integer duracionMinutos = 30;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TipoCita tipoCita = TipoCita.CONSULTA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal costo = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "dia")
    private LocalDate dia;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaCreacion = now;
        this.fechaActualizacion = now;
        if (this.fecha != null) {
            this.dia = this.fecha.toLocalDate();
        }
    }

    @PreUpdate
    void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        if (this.fecha != null) {
            this.dia = this.fecha.toLocalDate();
        }
    }

    public enum TipoCita {
        CONSULTA,
        CONTROL,
        URGENCIA,
        TELEMEDICINA
    }

    public enum EstadoCita {
        PROGRAMADA,
        EN_CURSO,
        CANCELADA,
        COMPLETADA
    }
}
