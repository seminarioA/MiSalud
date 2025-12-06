package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "seguros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguro")
    private Long id;

    @Column(name = "nombre_aseguradora", nullable = false, length = 100)
    private String nombreAseguradora;

    @Column(name = "tipo_seguro", nullable = false, length = 50)
    private String tipoSeguro; // EPS, SIS, PARTICULAR, ETC.

    @Column(name = "cobertura_porcentaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal coberturaPorcentaje; // e.g. 0.80 for 80%

    @Column(name = "copago_fijo", precision = 10, scale = 2)
    private BigDecimal copagoFijo; // Fixed amount patient pays regardless of percentage

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
