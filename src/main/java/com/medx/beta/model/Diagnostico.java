package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diagnosticos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diagnostico")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_registro", nullable = false,
            foreignKey = @ForeignKey(name = "fk_diagnosticos__registro_citas_medicas"))
    private RegistroCitaMedica registro;

    @Column(name = "codigo_icd10", nullable = false, length = 10)
    private String codigoIcd10;

    @Column(name = "descripcion", length = 255)
    private String descripcion;
}

