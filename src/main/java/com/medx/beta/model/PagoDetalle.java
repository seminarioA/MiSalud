package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pago_Detalle")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PagoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "facturacion_id", foreignKey = @ForeignKey(name = "fk_pago_detalle__fact"))
    private Facturacion facturacion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Metodo metodo;

    private LocalDateTime fechaPago;
    private String referenciaExterna;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Metodo { EFECTIVO, TARJETA, TRANSFERENCIA, SEGURO }
}
