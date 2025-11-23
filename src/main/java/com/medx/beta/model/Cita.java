package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false,
            foreignKey = @ForeignKey(name = "fk_citas__pacientes"))
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_doctor", nullable = false,
            foreignKey = @ForeignKey(name = "fk_citas__doctores"))
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_consultorio", nullable = false,
            foreignKey = @ForeignKey(name = "fk_citas__consultorios"))
    private Consultorio consultorio;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @Column(name = "hora_cita", nullable = false)
    private LocalTime horaCita;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoCita estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atencion", nullable = false, length = 15)
    private TipoAtencion tipoAtencion;

    @Column(name = "fecha_impresion")
    private LocalDateTime fechaImpresion;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;

    @Column(name = "monto_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoDescuento;

    @Column(name = "costo_neto_cita", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoNetoCita;

    public enum EstadoCita {
        PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA, NO_ASISTIO
    }

    public enum TipoAtencion {
        PRESENCIAL, TELECONSULTA, DOMICILIARIA
    }
}

