package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Facturacion", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cita_id"}, name = "uk_facturacion_cita")
})
@Getter
@Setter
public class Facturacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facturacionId;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_facturacion_cita"))
    private CitaMedica cita;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "El monto total no puede ser negativo")
    @Digits(integer = 8, fraction = 2)
    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "El monto pagado no puede ser negativo")
    @Digits(integer = 8, fraction = 2)
    @Column(name = "monto_pagado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;

    public enum TipoPago {
        EFECTIVO, TARJETA, TRANSFERENCIA, SEGURO, CORTESIA
    }

    public enum EstadoPago {
        Pendiente, Parcial, Pagado, Reembolsado, Anulado
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false, length = 20)
    private TipoPago tipoPago = TipoPago.EFECTIVO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false, length = 20)
    private EstadoPago estadoPago = EstadoPago.Pendiente;

    @Column(name = "fecha_emision", nullable = false, updatable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        this.fechaEmision = java.time.LocalDateTime.now();
        this.fechaActualizacion = java.time.LocalDateTime.now();
        validarMontos();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = java.time.LocalDateTime.now();
        validarMontos();
    }

    private void validarMontos() {
        if (montoPagado != null && montoTotal != null && montoPagado.compareTo(montoTotal) > 0) {
            throw new IllegalArgumentException("El monto pagado no puede ser mayor al monto total");
        }
    }
}
