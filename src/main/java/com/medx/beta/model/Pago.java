package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cita", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pagos__citas"))
    private Cita cita;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transaccion", nullable = false, length = 10)
    private TipoTransaccion tipoTransaccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false, length = 10)
    private EstadoPago estadoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 30)
    private MetodoPago metodoPago;

    @Column(name = "codigo_operacion", length = 50)
    private String codigoOperacion;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    public enum TipoTransaccion {
        INGRESO, EGRESO
    }

    public enum EstadoPago {
        COMPLETADO, PENDIENTE, ANULADO, REVERTIDO
    }

    public enum MetodoPago {
        EFECTIVO,
        YAPE,
        PLIN,
        TUNKI,
        BIM,
        AGORA,
        TARJETA_CREDITO,
        TARJETA_DEBITO,
        TRANSFERENCIA_BCP,
        TRANSFERENCIA_BBVA,
        TRANSFERENCIA_INTERBANK,
        TRANSFERENCIA_SCOTIABANK,
        TRANSFERENCIA_BANCO_NACION,
        DEPOSITO_AGENTE,
        OTRO
    }
}

