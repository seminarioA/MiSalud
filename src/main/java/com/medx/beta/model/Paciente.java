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
    @JoinColumn(name = "id_persona", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_pacientes__personas"))
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seguro", foreignKey = @ForeignKey(name = "fk_pacientes__seguros"))
    private Seguro seguro;

    @Builder.Default
    @Column(name = "tipo_seguro", nullable = false, length = 50)
    private String tipoSeguro = "PARTICULAR";
}
