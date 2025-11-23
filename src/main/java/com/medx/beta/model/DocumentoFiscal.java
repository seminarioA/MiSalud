package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documentos_fiscales", uniqueConstraints = {
        @UniqueConstraint(name = "uk_documentos_fiscales__serie_correlativo", columnNames = {"serie", "correlativo"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pago", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_documentos_fiscales__pagos"))
    private Pago pago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante", nullable = false, length = 10)
    private TipoComprobante tipoComprobante;

    @Column(name = "serie", nullable = false, length = 4)
    private String serie;

    @Column(name = "correlativo", nullable = false)
    private Integer correlativo;

    @Column(name = "ruc_cliente", length = 11)
    private String rucCliente;

    @Column(name = "nombre_cliente_fiscal", nullable = false, length = 100)
    private String nombreClienteFiscal;

    public enum TipoComprobante {
        BOLETA, FACTURA
    }
}

