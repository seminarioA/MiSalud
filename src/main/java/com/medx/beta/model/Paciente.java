package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_pacientes__personas"))
    private Persona persona;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_seguro", nullable = false, length = 15)
    private TipoSeguro tipoSeguro;

    public enum TipoSeguro {
        SIS, ESSALUD, EPS, PARTICULAR, SOAT, SCTR, OTRO
    }
}

