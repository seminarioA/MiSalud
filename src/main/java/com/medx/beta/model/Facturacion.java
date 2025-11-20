package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Facturacion",
        uniqueConstraints = @UniqueConstraint(name = "uq_facturacion__cita", columnNames = "cita_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Facturacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "cita_id", foreignKey = @ForeignKey(name = "fk_facturacion__cita"))
    private CitaMedica cita;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPago tipoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPago estadoPago;

    private LocalDateTime fechaEmision;
    private LocalDateTime fechaActualizacion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "facturacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoDetalle> pagos;

    public enum TipoPago { EFECTIVO, TARJETA, TRANSFERENCIA, SEGURO, CORTESIA }
    public enum EstadoPago { PENDIENTE, PAGADO, PARCIAL, REEMBOLSADO }
}

